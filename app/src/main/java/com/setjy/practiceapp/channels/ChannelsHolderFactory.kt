package com.setjy.practiceapp.channels

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.holders.StreamItemViewHolder
import com.setjy.practiceapp.recycler.holders.TopicItemViewHolder
import com.setjy.practiceapp.recycler.items.StreamItemUI

class ChannelsHolderFactory(
    private val onStreamClickAction: (stream: StreamItemUI) -> Unit,
    private val onTopicNameClickAction: (topicName: String, streamName: String) -> Unit
) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_stream -> StreamItemViewHolder(view).apply {
                onStreamClick = onStreamClickAction
            }
            R.layout.item_topic -> TopicItemViewHolder(view).apply {
                onTopicClick = onTopicNameClickAction
            }
            R.layout.item_topic_shimmer -> BaseViewHolder<ViewTyped>(view)
            else -> null
        }
    }
}