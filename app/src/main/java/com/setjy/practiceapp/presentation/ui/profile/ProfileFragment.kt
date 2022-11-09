package com.setjy.practiceapp.presentation.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentProfileBinding
import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile), MviView<ProfileState, BaseEffect> {

    private val viewModel: MviViewModel<ProfileAction, ProfileState, BaseEffect> by viewModels { ProfileViewModelFactory() }

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(this)
        viewModel.accept(ProfileAction.LoadOwnUser)
    }

    override fun renderState(state: ProfileState) {
        binding.shimmer.root.isVisible = state.isLoading
        val user = state.userItemUI
        if (user != null) {
            with(binding) {
                tvFullName.text = user.fullName
                tvStatus.apply {
                    text = user.status.name.lowercase()
                    setTextColor(resources.getColor(user.status.color, null))
                }
                Glide.with(requireContext())
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(ivAvatar)
            }
        }
    }

    override fun renderEffect(effect: BaseEffect) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }
}