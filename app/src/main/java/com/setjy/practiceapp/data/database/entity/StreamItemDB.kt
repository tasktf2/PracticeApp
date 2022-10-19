package com.setjy.practiceapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

@Entity(tableName = "stream")
data class StreamItemDB(
    @PrimaryKey
    @ColumnInfo(name = "stream_id")
    val streamId: Int,
    @ColumnInfo(name = "stream_name")
    val streamName: String,
    @ColumnInfo(name = "is_subscribed")
    val isSubscribed: Boolean = false,
    @ColumnInfo(name = "background_color")
    val backgroundColor: String?
)