package com.setjy.practiceapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

@Entity(tableName = "topic")
data class TopicItemUI(
    @PrimaryKey
    @ColumnInfo(name = "topic_id")
    val topicId: Int,
    @ColumnInfo(name = "topic_name")
    val topicName: String,
    @ColumnInfo(name = "message_count")
    val messageCount: Int = 0,
    @ColumnInfo(name = "parent_id")
    val parentId: Int,
    @ColumnInfo(name = "parent_name")
    val parentName: String,
    @ColumnInfo(name = "background_color")
    val backgroundColor: String?,
    @ColumnInfo(name = "is_found")
    val isFound: Boolean = false,
    @ColumnInfo(name = "uid")
    override val uid: Int = topicId,
    @ColumnInfo(name = "view_type")
    override val viewType: Int = R.layout.item_topic
) : ViewTyped