package com.setjy.practiceapp.channels

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.holders.StreamItemViewHolder
import com.setjy.practiceapp.recycler.holders.TopicItemViewHolder

class ChannelsHolderFactory(
    private val onStreamClickAction: (streamName: String) -> Unit,
    private val onTopicNameClickAction: (topicName: String) -> Unit
) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_stream -> StreamItemViewHolder(view).apply {
                onStreamClick = onStreamClickAction
            }
            R.layout.item_topic -> TopicItemViewHolder(view).apply {
                onTopicClick = onTopicNameClickAction
            }
            else -> null
        }
    }
}