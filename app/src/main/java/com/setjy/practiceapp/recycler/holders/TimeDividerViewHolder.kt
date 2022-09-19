package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemTimeDividerBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.TimeUI
import com.setjy.practiceapp.util.getTimeStamp


class TimeDividerViewHolder(view: View) : BaseViewHolder<TimeUI>(view) {

    private val binding: ItemTimeDividerBinding by viewBinding()

    override fun bind(item: TimeUI) {
        binding.tvTimeDivider.text = getTimeStamp(item.timeStamp)
    }

}
