package com.setjy.practiceapp.presentation.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import javax.inject.Inject

class ChannelsFragment : Fragment(), MviView<ChannelsState, BaseEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<ChannelsAction, ChannelsState, BaseEffect>
    private val viewModel: MviViewModel<ChannelsAction, ChannelsState, BaseEffect> by viewModels {
        mviViewModelFactory
    }

    private var screenState by mutableStateOf(ChannelsState())


    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addChannelsComponent()
            channelsComponent?.inject(this@ChannelsFragment)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ZulipTheme {
                    ChannelsScreen(
                        screenState,
                        onStreamClick = ::onStreamClick,
                        onTopicClick = ::onTopicClick,
                        onSearchValueChange = { viewModel.accept(ChannelsAction.SearchStreams(it)) },
                        search = screenState.search,
                        onPageSelected = { page ->
                            viewModel.accept(
                                ChannelsAction.LoadStreams(
                                    isSubscribed = page == Page.SUBSCRIBED.ordinal
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }

    override fun renderState(state: ChannelsState) {
        screenState = state
    }

    override fun renderEffect(effect: BaseEffect) = Unit

    private fun onStreamClick(streamItemUI: StreamItemUI, isSubscribed: Boolean) {
        viewModel.accept(ChannelsAction.ToggleStream(streamItemUI, isSubscribed))
    }

    private fun onTopicClick(topicNameFromClick: String, streamName: String) {
        val bundle: Bundle = bundleOf(STREAM_BUNDLE_KEY to arrayOf(streamName, topicNameFromClick))
        findNavController().navigate(R.id.action_channels_fragment_to_topicFragment, bundle)
    }

    companion object {
        const val STREAM_BUNDLE_KEY: String = "STREAM_BUNDLE_KEY"

        const val STREAM_ARRAY_INDEX: Int = 0
        const val TOPIC_ARRAY_INDEX: Int = 1
    }

}