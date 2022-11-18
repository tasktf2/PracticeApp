package com.setjy.practiceapp.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.FragmentProfileBinding
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.fragment_profile),
    MviView<ProfileState, ProfileEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<ProfileAction, ProfileState, ProfileEffect>

    private val viewModel: MviViewModel<ProfileAction, ProfileState, ProfileEffect> by viewModels {
        mviViewModelFactory
    }

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addProfileComponent()
            profileComponent?.inject(this@ProfileFragment)
        }
        super.onAttach(context)
    }

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

    override fun renderEffect(effect: ProfileEffect) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }
}