package com.setjy.practiceapp.presentation.ui.people

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentPeopleBinding
import com.setjy.practiceapp.presentation.base.mvi.FragmentViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.recycler.Adapter
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PeopleFragment : Fragment(R.layout.fragment_people), MviView<PeopleState> {

    private val viewModel: FragmentViewModel<PeopleAction, PeopleState> by viewModels { PeopleViewModelFactory() }

    private val binding: FragmentPeopleBinding by viewBinding()

    private val holderFactory = PeopleHolderFactory()

    private val adapter = Adapter<ViewTyped>(holderFactory)

    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListOfUsers.adapter = adapter
        binding.rvListOfUsers.layoutManager = LinearLayoutManager(context)
        viewModel.bind(this)
        viewModel.accept(PeopleAction.LoadUsers)
    }

    override fun render(state: PeopleState) {
        binding.shimmer.isVisible = state.isLoading

        if (state.visibleUsers != null) {
            adapter.items = state.visibleUsers
            initUserSearch()
        }
    }

    private fun initUserSearch() {
        binding.etSearch.addTextChangedListener { text ->
            val query = text?.toString()?.trim().orEmpty()
            viewModel.accept(PeopleAction.SearchUsers(query))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
        viewModel.unbind()
    }
}