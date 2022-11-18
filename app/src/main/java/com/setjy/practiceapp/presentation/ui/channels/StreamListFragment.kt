package com.setjy.practiceapp.presentation.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.FragmentStreamListBinding
import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.base.recycler.Adapter
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.util.hideKeyboard
import javax.inject.Inject

class StreamListFragment : Fragment(R.layout.fragment_stream_list),
    MviView<ChannelsState, BaseEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<ChannelsAction, ChannelsState, BaseEffect>

    private val viewModel: MviViewModel<ChannelsAction, ChannelsState, BaseEffect> by viewModels {
        mviViewModelFactory
    }
    private val holderFactory: ChannelsHolderFactory = ChannelsHolderFactory(
        this::onStreamClick,
        this::onTopicClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private val page: Page by lazy { arguments?.getSerializable(ARG_PAGE) as Page }

    private val binding: FragmentStreamListBinding by viewBinding()

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addChannelsComponent()
            channelsComponent?.inject(this@StreamListFragment)
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStreamList.layoutManager = LinearLayoutManager(context)
        binding.rvStreamList.adapter = adapter

        viewModel.bind(this)
        viewModel.accept(ChannelsAction.LoadStreams(isSubscribed = page == Page.SUBSCRIBED))
        subscribeToSearch()
    }

    override fun renderState(state: ChannelsState) {
        binding.shimmer.isVisible = state.isLoading

        if (state.visibleItems != null) {
            adapter.items = state.visibleItems
        }
    }

    override fun renderEffect(effect: BaseEffect) = Unit

    private fun subscribeToSearch() {
        parentFragmentManager.setFragmentResultListener(
            ChannelsFragment.QUERY_REQUEST_KEY,
            this
        ) { _, bundle ->
            val query = bundle.getString(ChannelsFragment.QUERY_BUNDLE_KEY).orEmpty()
            viewModel.accept(ChannelsAction.SearchStreams(query))
            hideKeyboard()
        }
    }

    private fun onStreamClick(streamItemUI: StreamItemUI) {
        viewModel.accept(ChannelsAction.ToggleStream(streamItemUI))
    }

    private fun onTopicClick(topicNameFromClick: String, streamName: String) {
        val bundle: Bundle = bundleOf(STREAM_BUNDLE_KEY to arrayOf(streamName, topicNameFromClick))
        findNavController().navigate(R.id.action_channels_fragment_to_topicFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }

    override fun onDetach() {
        (requireContext().applicationContext as ZulipApp).clearChannelsComponent()
        super.onDetach()
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

