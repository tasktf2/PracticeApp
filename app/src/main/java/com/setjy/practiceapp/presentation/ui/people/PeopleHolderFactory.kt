package com.setjy.practiceapp.presentation.ui.people

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.recycler.base.BaseViewHolder
import com.setjy.practiceapp.presentation.base.recycler.base.HolderFactory
import com.setjy.practiceapp.presentation.ui.people.holder.UserViewHolder

class PeopleHolderFactory : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder(view)
            else -> null
        }
    }
}