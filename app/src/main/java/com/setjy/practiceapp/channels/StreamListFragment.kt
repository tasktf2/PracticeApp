package com.setjy.practiceapp.channels

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.Data
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentStreamListBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.StreamItemUI
import com.setjy.practiceapp.recycler.items.TopicItemUI
import com.setjy.practiceapp.util.hideKeyboard
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class StreamListFragment : Fragment(R.layout.fragment_stream_list) {

    private var streamName: String = ""

    private val holderFactory: ChannelsHolderFactory = ChannelsHolderFactory(
        this::onStreamClick,
        this::onTopicClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private val page: Page by lazy { arguments?.getSerializable(ARG_PAGE) as Page }

    private lateinit var disposable: Disposable

    private val binding: FragmentStreamListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStreamList.layoutManager = LinearLayoutManager(context)
        binding.rvStreamList.adapter = adapter
        adapter.putItems(page)
        subscribeToSearch()
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

    private fun Adapter<ViewTyped>.putItems(page: Page?) {
        val mutableList: MutableList<ViewTyped> = mutableListOf()

        disposable = Data.getStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                if (page?.name == Page.SUBSCRIBED.name) {
                    items.forEach { stream ->
                        if ((stream as StreamItemUI).isSubscribed) {
                            mutableList.add(stream)
                        }
                    }
                } else {
                    items.forEach { stream ->
                        mutableList.add(stream)
                    }
                }
                this.items = mutableList
            }
    }

    private fun onStreamClick(streamNameFromClick: String) {

        streamName = streamNameFromClick

        adapter.items.apply {
            val stream = filterIsInstance<StreamItemUI>()
                .find { it.streamName == streamNameFromClick }!!
                .apply { isExpanded = !isExpanded }

            val currentTopics: List<TopicItemUI> = stream.listOfTopics
            val topicIndex: Int = indexOf(stream) + 1 // topic goes below stream
            val adapterItems: MutableList<ViewTyped> = this.toMutableList()

            if (stream.isExpanded) {
                adapterItems.addAll(topicIndex, currentTopics)
            } else {
                currentTopics.forEach { topic ->
                    adapterItems.removeIf {
                        it is TopicItemUI && it.topicName == topic.topicName
                    }
                }
            }
            adapter.items = adapterItems
        }
    }

    private fun onTopicClick(topicNameFromClick: String) {
        val bundle: Bundle = bundleOf(STREAM_BUNDLE_KEY to arrayOf(streamName, topicNameFromClick))
        findNavController().navigate(R.id.action_channels_fragment_to_topicFragment, bundle)
    }

    companion object {
        private const val ARG_PAGE: String = "ARG_PAGE"
        const val STREAM_BUNDLE_KEY: String = "STREAM_BUNDLE_KEY"
        const val STREAM_ARRAY_INDEX: Int = 0
        const val TOPIC_ARRAY_INDEX: Int = 1

        fun newInstance(page: Page): StreamListFragment {
            val args = Bundle()
            args.putSerializable(ARG_PAGE, page)
            val fragment = StreamListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
