package com.setjy.practiceapp.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.databinding.FragmentProfileBinding
import com.setjy.practiceapp.util.getImageViewFromUrl
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

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    private fun getOwnUser() {
        disposable += Data.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess { hideLoading() }
            .doOnSubscribe { showLoading() }
            .subscribe { item ->
                with(binding) {
                    getImageViewFromUrl(item.avatarUrl, ivAvatar)
                    tvFullName.text = item.fullName
                    tvStatus.apply {
                        text = item.status.name.lowercase()
                        setTextColor(resources.getColor(item.status.color, null))
                    }
                }
            }
    }

    private fun hideLoading() {
        binding.shimmer.root.apply {
            stopShimmer()
        }.isVisible = false
    }

    private fun showLoading() {
        binding.shimmer.root.showShimmer(true)
    }
}