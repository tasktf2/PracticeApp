package com.setjy.practiceapp.recycler.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.holders.TimeDividerViewHolder

abstract class HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewTyped> {

    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    override fun invoke(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view: View = parent.inflate(viewType)
        return when (viewType) {
            R.layout.item_time_divider -> TimeDividerViewHolder(view)

            else -> checkNotNull(createViewHolder(view, viewType)) {
                "unknown viewType" + parent.resources.getResourceName(viewType)
            }
        } as BaseViewHolder<ViewTyped>
    }
}

fun <T : View> View.inflate(
    @LayoutRes layout: Int,
    root: ViewGroup? = this as? ViewGroup,
    attachToRoot: Boolean = false
): T {
    return LayoutInflater.from(context).inflate(layout, root, attachToRoot) as T
}
