package com.setjy.practiceapp.recycler.items

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

@Entity(tableName = "stream")
data class StreamItemUI(
    @PrimaryKey
        (autoGenerate = true)
    val streamId: Int,
    val streamName: String,
    val isSubscribed: Boolean = false,
    val backgroundColor: String?,
    val isFound: Boolean = false,
    var isExpanded: Boolean = false,
    override val viewType: Int = R.layout.item_stream
) : ViewTyped