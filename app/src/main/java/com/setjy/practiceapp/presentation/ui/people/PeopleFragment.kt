package com.setjy.practiceapp.presentation.ui.people

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.FragmentPeopleBinding
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.base.recycler.Adapter
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import javax.inject.Inject

class PeopleFragment : Fragment(R.layout.fragment_people), MviView<PeopleState, PeopleEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<PeopleAction, PeopleState, PeopleEffect>

    private val viewModel: MviViewModel<PeopleAction, PeopleState, PeopleEffect> by viewModels {
        mviViewModelFactory
    }

    private val binding: FragmentPeopleBinding by viewBinding()

    private val holderFactory = PeopleHolderFactory()

    private val adapter = Adapter<ViewTyped>(holderFactory)

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addPeopleComponent()
            peopleComponent?.inject(this@PeopleFragment)
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListOfUsers.adapter = adapter
        binding.rvListOfUsers.layoutManager = LinearLayoutManager(context)
        viewModel.bind(this)
        viewModel.accept(PeopleAction.LoadUsers)
        initUserSearch()
    }

    override fun renderState(state: PeopleState) {
        binding.shimmer.isVisible = state.isLoading

        if (state.visibleUsers != null) {
            adapter.items = state.visibleUsers
        }
    }

    override fun renderEffect(effect: PeopleEffect) = Unit

    private fun initUserSearch() {
        binding.etSearch.addTextChangedListener { text ->
            val query = text?.toString()?.trim().orEmpty()
            viewModel.accept(PeopleAction.SearchUsers(query))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }
}