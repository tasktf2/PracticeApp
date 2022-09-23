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
        adapter.putItems(page)
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
                adapter.putItems(page)
            }
            hideKeyboard()
        }
    }

    private fun Adapter<ViewTyped>.putItems(page: Page) {
        disposable += Data.getStreamsAndTopics(page == Page.SUBSCRIBED)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { hideLoading() }
            .doOnSubscribe { showLoading() }
            .subscribe({ items -> this.items = items },
                { error ->
                    hideLoading()
                    Log.d("xxx", "get streams and topics error: $error")
                }
            )
    }

    private fun hideLoading() {
        binding.shimmer.apply { stopShimmer() }.isVisible = false
    }

    private fun showLoading() {
        binding.shimmer.showShimmer(true)
    }

    private fun onStreamClick(streamNameFromClick: String) {

        adapter.apply {
            val stream = items.filterIsInstance<StreamItemUI>()
                .find { it.streamName == streamNameFromClick }!!
                .apply { isExpanded = !isExpanded }

            val currentTopics: List<TopicItemUI> = stream.listOfTopics
            val topicIndex: Int = items.indexOf(stream) + 1 // topic goes below stream
            val adapterItems: MutableList<ViewTyped> = items.toMutableList()
            if (stream.isExpanded) {
                adapterItems.addAll(topicIndex, currentTopics)
            } else {
                currentTopics.forEach { topic ->
                    adapterItems.removeIf {
                        it is TopicItemUI && it.topicName == topic.topicName
                    }
                }
            }
            items = adapterItems
        }
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
