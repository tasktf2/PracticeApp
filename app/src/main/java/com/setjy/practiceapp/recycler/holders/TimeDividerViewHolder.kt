package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.ItemTimeDividerBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.TimeUI
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*



class TimeDividerViewHolder(view: View) : BaseViewHolder<TimeUI>(view) {

    private val binding: ItemTimeDividerBinding by viewBinding()

    override fun bind(item: TimeUI) {
        binding.tvTimeDivider.text = getTimeStamp(item.timeStamp)
    }

    private fun getTimeStamp(timeStamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM", Locale("ru"))
        val date = Date(timeStamp)
        val dfs = DateFormatSymbols()
        val shortMonths = listOf(
            "Янв",
            "Фев",
            "Мар",
            "Апр",
            "Мая",
            "Июн",
            "Июл",
            "Авг",
            "Сен",
            "Окт",
            "Ноя",
            "Дек"
        )
        dfs.shortMonths = shortMonths.toTypedArray()
        sdf.dateFormatSymbols = dfs
        return sdf.format(date)
    }
}
