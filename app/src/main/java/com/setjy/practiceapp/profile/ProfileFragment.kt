package com.setjy.practiceapp.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.recycler.items.UserItemUI
import com.setjy.practiceapp.databinding.FragmentProfileBinding
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOwnUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()

    }

    private fun getOwnUser() {
        disposable += Data.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext { hideLoading() }
            .doOnSubscribe { showLoading() }
            .subscribe { user -> bindUser(user) }
    }

    private fun bindUser(user: UserItemUI) {
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

    private fun hideLoading() {
        with(binding.shimmer.root) {
            stopShimmer()
            isVisible = false
        }
    }

    private fun showLoading() {
        with(binding.shimmer.root) {
            startShimmer()
            isVisible = true
        }
    }
}