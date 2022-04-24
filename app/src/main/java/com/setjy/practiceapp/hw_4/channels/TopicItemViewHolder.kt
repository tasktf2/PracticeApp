package com.setjy.practiceapp.hw_4.channels

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.ItemTopicBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped

//todo в айтемы
class TopicItemUI(
    val topicName: String,
    val messageCount: Int,
    override val viewType: Int = R.layout.item_topic
) : ViewTyped

class TopicItemViewHolder(val view: View) : BaseViewHolder<TopicItemUI>(view) {

    private val binding: ItemTopicBinding by viewBinding()

    var onTopicClick: ((topicName: String) -> Unit)? = null

    override fun bind(item: TopicItemUI) {
        binding.tvTopicName.text = item.topicName
        binding.tvMessageCount.text = item.messageCount.toString()
        binding.topicContainer.setOnClickListener {
            onTopicClick?.invoke(item.topicName)
        }
    }
}