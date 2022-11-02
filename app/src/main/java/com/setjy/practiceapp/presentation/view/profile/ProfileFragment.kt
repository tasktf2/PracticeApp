package com.setjy.practiceapp.presentation.view.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.jakewharton.rxrelay3.PublishRelay
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentProfileBinding
import com.setjy.practiceapp.presentation.view.base.MviView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ProfileFragment : Fragment(R.layout.fragment_profile), MviView<Action, State> {

    private val viewModel: ProfileViewModel by viewModels()

    private val binding: FragmentProfileBinding by viewBinding()

    private val disposable: CompositeDisposable = CompositeDisposable()

    override val _actions: PublishRelay<Action> = PublishRelay.create()

    override val actions: Observable<Action> = _actions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(this)
        _actions.accept(Action.LoadingOwnUser)
    }

    override fun render(state: State) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
        viewModel.unbind()
    }
}