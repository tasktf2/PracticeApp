package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemTopicBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.TopicItemUI

class TopicItemViewHolder(val view: View) : BaseViewHolder<TopicItemUI>(view) {

    private val binding: ItemTopicBinding by viewBinding()

    var onTopicClick: ((topicName: String, streamName: String) -> Unit)? = null

    override fun bind(item: TopicItemUI) {
        binding.tvTopicName.text = item.topicName
        binding.tvMessageCount.text = item.messageCount.toString()
        binding.topicContainer.setOnClickListener {
            onTopicClick?.invoke(item.topicName, item.parent)
        }
    }
}