package com.setjy.practiceapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
data class StreamEntity(
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