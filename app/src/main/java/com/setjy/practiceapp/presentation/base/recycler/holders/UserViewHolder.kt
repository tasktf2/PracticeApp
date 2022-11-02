package com.setjy.practiceapp.presentation.base.recycler.holders

import android.view.View
import androidx.core.content.res.ResourcesCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.setjy.practiceapp.databinding.ItemUserBinding
import com.setjy.practiceapp.presentation.base.recycler.base.BaseViewHolder
import com.setjy.practiceapp.presentation.model.UserItemUI

class UserViewHolder(val view: View) : BaseViewHolder<UserItemUI>(view) {

    private val binding: ItemUserBinding by viewBinding()

    override fun bind(item: UserItemUI) {
        with(binding) {
            tvFullName.text = item.fullName
            userEmail.text = item.userEmail
            vIndicator.foreground =
                ResourcesCompat.getDrawable(view.resources, item.status.color, null)
            Glide.with(view)
                .load(item.avatarUrl)
                .centerCrop()
                .into(ivAvatar)

        }
    }
}