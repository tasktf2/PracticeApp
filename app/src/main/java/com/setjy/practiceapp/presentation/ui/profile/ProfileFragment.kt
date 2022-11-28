package com.setjy.practiceapp.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val viewModel: ProfileViewModel by viewModels { profileViewModelFactory }

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

        viewModel.observableStates.onEach(::renderState).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.dispatch(ProfileAction.LoadOwnUser)
    }

    private fun renderState(state: ProfileState) {
        binding.shimmer.root.isVisible = state.isLoading
        bindUser(user = state.userItemUI)
    }

    private fun bindUser(user: UserItemUI?) {
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
}