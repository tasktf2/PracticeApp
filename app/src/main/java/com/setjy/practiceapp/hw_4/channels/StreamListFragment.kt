package com.setjy.practiceapp.hw_4.channels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentStreamListBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.base.ViewTyped

class StreamListFragment : Fragment() {

    private var streamName: String = ""

    private val holderFactory: ChannelsHolderFactory = ChannelsHolderFactory(
        this::onStreamClick,
        this::onTopicClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private var page: Page? = null

    private val binding: FragmentStreamListBinding by viewBinding()

    //todo в дата
    private val tempMapOfStreams: MutableMap<StreamItemUI, MutableList<TopicItemUI>> =
        mutableMapOf()

    fun newInstance(page: Page): StreamListFragment {
        val args = Bundle()
        args.putSerializable(ARG_PAGE, page)
        val fragment = StreamListFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            page = arguments?.getSerializable(ARG_PAGE) as Page
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stream_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStreamList.layoutManager = LinearLayoutManager(context)
        binding.rvStreamList.adapter = adapter
        loadStreams()
        adapter.putItems(page)
    }

    private fun loadStreams() {
        tempMapOfStreams.putAll(
            mapOf(
                StreamItemUI("#general", true) to mutableListOf(
                    TopicItemUI(
                        "Testing",
                        1240
                    ), TopicItemUI(
                        "bruh1",
                        123
                    ), TopicItemUI(
                        "bruh2",
                        123
                    )
                ),
                StreamItemUI("#Development") to mutableListOf(
                    TopicItemUI(
                        "bruh3",
                        123
                    )
                ), StreamItemUI("#Design") to mutableListOf(
                    TopicItemUI(
                        "bruh4",
                        123
                    )
                ), StreamItemUI("#PR", true) to mutableListOf(
                    TopicItemUI(
                        "bruh5",
                        123
                    )
                )


            )
        )
    }

    private fun Adapter<ViewTyped>.putItems(page: Page?) {
        val list: MutableList<ViewTyped> = mutableListOf()
        if (page?.subscribed!!) {
            tempMapOfStreams.forEach { (key, _) ->
                if (key.isSubscribed) {
                    list.add(key)
                }
            }
        } else {
            tempMapOfStreams.forEach { (key, _) ->
                list.apply {
                    add(key)
                }
            }
        }
        this.items = list
    }

    private fun onStreamClick(streamNameFromClick: String, arrow:ImageView?) {
        streamName = streamNameFromClick
        val currentStream: StreamItemUI =
            tempMapOfStreams.keys.find { it.streamName == streamName }!!
        val currentTopics: MutableList<TopicItemUI> = tempMapOfStreams[currentStream]!!
        val topicIndex: Int = adapter.items.indexOf(currentStream) + 1
        val adapterItems: MutableList<ViewTyped> = adapter.items.toMutableList()

        if (!adapterItems.containsAll(currentTopics)) {
                arrow!!.isSelected=true
            adapterItems.addAll(topicIndex, currentTopics)
        } else {
            arrow!!.isSelected=false
            adapterItems.removeAll(currentTopics)
        }
        adapter.items = adapterItems
        adapter.notifyItemRangeChanged(topicIndex, topicIndex + currentTopics.size)
    }

    private fun onTopicClick(topicNameFromClick: String) {
        val bundle: Bundle = bundleOf(STREAM_BUNDLE_KEY to arrayOf(streamName, topicNameFromClick))
        findNavController().navigate(R.id.action_channels_fragment_to_chatFragment2, bundle)
        Log.d("xxx","${findNavController().currentDestination}")

    }

    companion object {
        private const val ARG_PAGE: String = "ARG_PAGE"
        const val STREAM_BUNDLE_KEY: String = "STREAM_BUNDLE_KEY"
    }
}
