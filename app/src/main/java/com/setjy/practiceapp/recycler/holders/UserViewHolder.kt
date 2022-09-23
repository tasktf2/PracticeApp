package com.setjy.practiceapp.recycler.holders

import android.view.View
import androidx.core.content.res.ResourcesCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemUserBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.UserItemUI
import com.setjy.practiceapp.util.getImageViewFromUrl

class UserViewHolder(val view: View) : BaseViewHolder<UserItemUI>(view) {

    private val binding: ItemUserBinding by viewBinding()

    override fun bind(item: UserItemUI) {
        with(binding) {
            tvFullName.text = item.fullName
            userEmail.text = item.userEmail
            vIndicator.foreground =
                ResourcesCompat.getDrawable(view.resources, item.status.color, null)
            getImageViewFromUrl(view, item.avatarUrl, ivAvatar)
        }
    }
}