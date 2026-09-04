package com.setjy.practiceapp.presentation.ui.topic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.ui.channels.ChannelsFragment.Companion.STREAM_ARRAY_INDEX
import com.setjy.practiceapp.presentation.ui.channels.ChannelsFragment.Companion.STREAM_BUNDLE_KEY
import com.setjy.practiceapp.presentation.ui.channels.ChannelsFragment.Companion.TOPIC_ARRAY_INDEX
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import javax.inject.Inject

class TopicFragment : Fragment(), MviView<TopicState, TopicEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<TopicAction, TopicState, TopicEffect>

    private val viewModel: MviViewModel<TopicAction, TopicState, TopicEffect> by viewModels {
        mviViewModelFactory
    }

    private val topicName: String by lazy {
        arguments?.getStringArray(STREAM_BUNDLE_KEY)
            ?.get(TOPIC_ARRAY_INDEX).orEmpty()
    }
    private val streamName: String by lazy {
        arguments?.getStringArray(STREAM_BUNDLE_KEY)
            ?.get(STREAM_ARRAY_INDEX).orEmpty()
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addTopicComponent()
            topicComponent?.inject(this@TopicFragment)
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
                    TopicScreen(
                        streamName = streamName,
                        topicName = topicName,
                        viewModel = viewModel,
                        onBackClick = {
                            findNavController().navigate(R.id.action_topicFragment_to_channels_fragment)
                        },
                    )
                }
            }
        }
    }


    override fun renderState(state: TopicState) {}

    override fun renderEffect(effect: TopicEffect) {}
}