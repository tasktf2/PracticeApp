package com.setjy.practiceapp.recycler.items

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

@Entity(tableName = "topic")
data class TopicItemUI(
    @PrimaryKey
    val topicId: Int,
    val topicName: String,
    val messageCount: Int = 0,
    val parentId: Int,
    val parentName: String,
    val backgroundColor: String?,
    val isFound: Boolean = false,
    override val viewType: Int = R.layout.item_topic
) : ViewTyped