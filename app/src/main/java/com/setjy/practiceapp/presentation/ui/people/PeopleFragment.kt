package com.setjy.practiceapp.presentation.ui.people

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import com.setjy.practiceapp.util.rememberShimmerProgress
import javax.inject.Inject

class PeopleFragment : Fragment(), MviView<PeopleState, PeopleEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<PeopleAction, PeopleState, PeopleEffect>

    private val viewModel: MviViewModel<PeopleAction, PeopleState, PeopleEffect> by viewModels {
        mviViewModelFactory
    }

    private var composeState by mutableStateOf(PeopleState())

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addPeopleComponent()
            peopleComponent?.inject(this@PeopleFragment)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ZulipTheme {
                    val shimmerProgress = rememberShimmerProgress()

                    UsersScreen(
                        state = composeState,
                        onValueChange = {
                            viewModel.accept(PeopleAction.SearchUsers(it))
                        },
                        progress = shimmerProgress,

                        )
                }

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(this)
        viewModel.accept(PeopleAction.LoadUsers)
    }

    override fun renderState(state: PeopleState) {

        composeState = state
    }

    override fun renderEffect(effect: PeopleEffect) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }
}