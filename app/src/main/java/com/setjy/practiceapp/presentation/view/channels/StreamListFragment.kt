package com.setjy.practiceapp.presentation.view.channels

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
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.FragmentStreamListBinding
import com.setjy.practiceapp.domain.usecase.stream.GetStreamsUseCase
import com.setjy.practiceapp.presentation.base.recycler.Adapter
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.model.StreamItemUI
import com.setjy.practiceapp.presentation.model.TopicItemUI
import com.setjy.practiceapp.util.hideKeyboard
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class StreamListFragment
//constructor(private val getStreamsUseCase: GetStreamsUseCase)
    :
    Fragment(R.layout.fragment_stream_list) {

    private val getStreamsUseCase by lazy { (requireContext().applicationContext as ZulipApp).globalDI.getStreamsUseCase }

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
        getStreams(page)
        subscribeToSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
                getStreams(page)
            }
            hideKeyboard()
        }
    }

    private fun getStreams(page: Page) {
        val isSubscribed = page == Page.SUBSCRIBED
        disposable += getStreamsUseCase.execute(params = GetStreamsUseCase.Params(isSubscribed))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showStreamsLoading() }
            .doFinally { hideStreamsLoading() }
            .subscribe(
                { streams ->
                    adapter.items = streams
                    if (streams.isNotEmpty()) {
                        hideStreamsLoading()
                    }
                },
                { error ->
                    Log.d("xxx", "get streams and topics error: $error")
                })
    }

    private fun hideStreamsLoading() {
        with(binding.shimmer) {
            stopShimmer()
            isVisible = false
        }
    }

    private fun showStreamsLoading() {
        with(binding.shimmer) {
            startShimmer()
            isVisible = true
        }
    }

    private fun onStreamClick(streamItemUI: StreamItemUI) {
        val stream = adapter.items.filterIsInstance<StreamItemUI>()
            .find { it.streamId == streamItemUI.streamId }!!
            .apply { isExpanded = !isExpanded }
        val adapterItems: MutableList<ViewTyped> = adapter.items.toMutableList()
        val topicIndex: Int = adapter.items.indexOf(stream) + 1 // topic goes below stream
        if (stream.isExpanded) {
            adapterItems.addAll(topicIndex, stream.listOfTopics)
        } else {
            stream.listOfTopics.forEach { topic ->
                adapterItems.removeIf {
                    it is TopicItemUI && it.topicId == topic.topicId
                }
            }
        }
        adapter.items = adapterItems
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