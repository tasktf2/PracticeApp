package com.setjy.practiceapp.channels

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.databinding.FragmentStreamListBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.StreamItemUI
import com.setjy.practiceapp.recycler.items.TopicItemUI
import com.setjy.practiceapp.recycler.items.TopicItemUIShimmer
import com.setjy.practiceapp.util.hideKeyboard
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class StreamListFragment : Fragment(R.layout.fragment_stream_list) {

    private val holderFactory: ChannelsHolderFactory = ChannelsHolderFactory(
        this::onStreamClick,
        this::onTopicClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private val page: Page by lazy { arguments?.getSerializable(ARG_PAGE) as Page }

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val binding: FragmentStreamListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStreamList.layoutManager = LinearLayoutManager(context)
        binding.rvStreamList.adapter = adapter
        adapter.getStreams(page)
        subscribeToSearch()
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    //todo fix bugs
    private fun subscribeToSearch() {
        parentFragmentManager.setFragmentResultListener(
            ChannelsFragment.QUERY_REQUEST_KEY,
            this
        ) { _, bundle ->
            val query = bundle.getString(ChannelsFragment.QUERY_BUNDLE_KEY).orEmpty()
            if (query.isNotEmpty()) {
                val searchResultsFilter = SearchResultsFilter()
                searchResultsFilter.setItems(adapter.items)
                searchResultsFilter.filterItems(query)
                adapter.items = searchResultsFilter.isFoundItems
            } else {
                adapter.getStreams(page)
            }
            hideKeyboard()
        }
    }

    private fun Adapter<ViewTyped>.getStreams(page: Page) {
        if (page == Page.SUBSCRIBED) {
            getSubscribedStreams()
        } else {
            getAllStreams()
        }
    }

    private fun Adapter<ViewTyped>.getSubscribedStreams() {
        disposable += Data.getSubscribedStreamsFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess { result ->
                if (result.isEmpty() || result.all { !it.isSubscribed }) {
                    Log.d("xxx", "result is false")
                    disposable += Data.getStreams(true)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterSuccess {
                            hideStreamsLoading()
                            insertStreams(it)
                        }
                        .doOnSubscribe { showStreamsLoading() }
                        .subscribe({ items -> this.items = items },
                            { error ->
                                hideStreamsLoading()
                                Log.d("xxx", "get streams and topics error: $error")
                            }
                        )
                }
            }
            .subscribe({ result ->
                Log.d("xxx", "result in get is: $result")
                this.items = result
            }, { error -> Log.d("xxx", "room get sub streams error: $error") })
    }

    private fun Adapter<ViewTyped>.getAllStreams() {
        disposable += Data.getAllStreamsFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess { result ->
                if (result.isEmpty() || result.all { it.isSubscribed }) {
                    Log.d("xxx", "result is false")
                    disposable += Data.getStreams(false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterSuccess {
                            hideStreamsLoading()
                            insertStreams(it)
                        }
                        .doOnSubscribe { showStreamsLoading() }
                        .subscribe({ items -> this.items = items },
                            { error ->
                                hideStreamsLoading()
                                Log.d("xxx", "get streams and topics error: $error")
                            }
                        )
                }
            }
            .subscribe({ result ->
                Log.d("xxx", "result in get from db is: $result")
                this.items = result
            }, { error -> Log.d("xxx", "room get sub streams error: $error") })
    }

    private fun insertStreams(streams: List<StreamItemUI>) {
        disposable += Data.insertAllStreamsToDB(streams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Log.d("xxx", "success in insert:$streams") },
                { e -> Log.d("xxx", "error in putStreams $e") })
    }

    private fun hideStreamsLoading() {
        binding.shimmer.apply { stopShimmer() }.isVisible = false
    }

    private fun showStreamsLoading() {
        binding.shimmer.showShimmer(true)
    }

    private fun onStreamClick(streamItemUI: StreamItemUI) {
        val stream = adapter.items.filterIsInstance<StreamItemUI>()
            .find { it.streamId == streamItemUI.streamId }!!
            .apply { isExpanded = !isExpanded }
        val shimmer = TopicItemUIShimmer()
        val adapterItems: MutableList<ViewTyped> = adapter.items.toMutableList()
        val topicIndex: Int = adapter.items.indexOf(stream) + 1 // topic goes below stream

        disposable += Data.getTopicsByStreamIdFromDB(streamItemUI.streamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess { result ->
                if (result.isEmpty()) {
                    disposable += Data.getTopics(stream)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            if (stream.isExpanded) {
                                adapterItems.add(topicIndex, shimmer)
                                adapter.items = adapterItems
                            }
                        }
                        .doAfterSuccess { topics ->
                            adapter.items = adapterItems.filterNot { it == shimmer }
                            disposable += Data.insertTopicsToDB(topics)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ Log.d("xxx", "insert topics successful: $topics") },
                                    { error -> Log.d("xxx", "error insert topics:$error ") })
                        }
                        .subscribe { topics ->
                            adapter.apply {
                                adapterItems.addAll(topicIndex, topics)
                                items = adapterItems
                            }
                        }
                }
            }
            .subscribe({ result ->
                adapter.apply {
                    if (stream.isExpanded) {
                        adapter.items = adapterItems.filterNot { it == shimmer }
                        adapterItems.addAll(topicIndex, result)
                    } else {
                        adapter.items = adapterItems.filterNot { it == shimmer }
                        result.forEach { topic ->
                            adapterItems.removeIf {
                                it is TopicItemUI && it.topicId == topic.topicId
                            }
                        }
                    }
                    items = adapterItems
                }
            },
                { error -> Log.d("xxx", "error in get topics from db: $error") })
    }

    private fun onTopicClick(topicNameFromClick: String, streamName: String) {
        val bundle: Bundle = bundleOf(STREAM_BUNDLE_KEY to arrayOf(streamName, topicNameFromClick))
        findNavController().navigate(R.id.action_channels_fragment_to_topicFragment, bundle)
    }

    companion object {
        private const val ARG_PAGE: String = "ARG_PAGE"
        const val STREAM_BUNDLE_KEY: String = "STREAM_BUNDLE_KEY"
        const val STREAM_ARRAY_INDEX: Int = 0
        const val TOPIC_ARRAY_INDEX: Int = 1

        fun newInstance(page: Page) = StreamListFragment().apply {
            arguments = bundleOf(ARG_PAGE to page)
        }
    }
}
