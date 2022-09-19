package com.setjy.practiceapp.people

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.databinding.FragmentPeopleBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.UserItemUI
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PeopleFragment : Fragment(R.layout.fragment_people) {

    private val binding: FragmentPeopleBinding by viewBinding()

    private val holderFactory = PeopleHolderFactory()

    private val adapter = Adapter<ViewTyped>(holderFactory)

    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListOfUsers.adapter = adapter
        binding.rvListOfUsers.layoutManager = LinearLayoutManager(context)
        getAllUsers()
    }

    private fun initUserSearch() {
        val adapterItems = adapter.items
        binding.etSearch.addTextChangedListener { text ->
            val query = text?.toString()?.trim().orEmpty()
            adapter.items = if (query.isEmpty()) {
                adapterItems
            } else {
                adapterItems.filter { item ->
                    (item as UserItemUI).fullName.contains(query, ignoreCase = true)
                }
            }
        }
    }

    private fun getAllUsers() {
        disposable += Data.getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess {
                hideLoading()
                initUserSearch()
            }
            .doOnSubscribe { showLoading() }
            .subscribe { users -> adapter.items = users }
    }

    private fun showLoading() = binding.shimmer.showShimmer(true)

    private fun hideLoading() {
        binding.shimmer.apply { stopShimmer() }.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}