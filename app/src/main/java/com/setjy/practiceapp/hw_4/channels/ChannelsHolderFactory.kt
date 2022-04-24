package com.setjy.practiceapp.hw_4.channels

import android.view.View
import android.widget.ImageView
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.HolderFactory

class ChannelsHolderFactory(
    private val onStreamClickAction: (streamName: String, arrow:ImageView?) -> Unit,
    private val onTopicNameClickAction: (topicName: String) -> Unit
) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_stream -> StreamItemViewHolder(view).apply {
                view.setOnClickListener {
//todo ???
                }
                onStreamClick = onStreamClickAction
            }
            R.layout.item_topic -> TopicItemViewHolder(view).apply {
                view.setOnClickListener {
//todo ???
                }
                onTopicClick = onTopicNameClickAction
            }
            else -> null
        }
    }
}