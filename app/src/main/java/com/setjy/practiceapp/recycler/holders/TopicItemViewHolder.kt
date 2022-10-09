package com.setjy.practiceapp.recycler.holders

import android.view.View
import androidx.core.graphics.toColorInt
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.ItemTopicBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.TopicItemUI

class TopicItemViewHolder(val view: View) : BaseViewHolder<TopicItemUI>(view) {

    private val binding: ItemTopicBinding by viewBinding()

    var onTopicClick: ((topicName: String, streamName: String) -> Unit)? = null

    override fun bind(item: TopicItemUI) {
        with(binding) {
            tvTopicName.text = item.topicName
            tvMessageCount.text = item.messageCount.toString()
            if (item.backgroundColor != null) {
                topicContainer.setBackgroundColor(item.backgroundColor.toColorInt())
            } else {
                topicContainer.setBackgroundColor(view.resources.getColor(R.color.app_cyan, null))
            }
            topicContainer.setOnClickListener {
                onTopicClick?.invoke(item.topicName, item.parentName)
            }
        }
    }
}