package com.setjy.practiceapp.presentation.base.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

class TimeUI(
    val timeStamp: Long,
    override val viewType: Int = R.layout.item_time_divider
) : ViewTyped