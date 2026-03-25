package com.setjy.practiceapp.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.util.rememberShimmerProgress
import com.setjy.practiceapp.util.shimmer
import javax.inject.Inject

class ProfileFragment : Fragment(), MviView<ProfileState, ProfileEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<ProfileAction, ProfileState, ProfileEffect>

    private val viewModel: MviViewModel<ProfileAction, ProfileState, ProfileEffect> by viewModels {
        mviViewModelFactory
    }

    private var composeState by mutableStateOf(ProfileState())

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addProfileComponent()
            profileComponent?.inject(this@ProfileFragment)
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
                ProfileScreen(state = composeState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bind(this)
        viewModel.accept(ProfileAction.LoadOwnUser)
    }

    override fun renderState(state: ProfileState) {
        composeState = state
    }

    override fun renderEffect(effect: ProfileEffect) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }
}

@Composable
fun ProfileScreen(state: ProfileState) {
    val shimmerProgress = rememberShimmerProgress()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_black)),
        contentAlignment = Alignment.Center
    ) {
        val user = state.userItemUI
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = user?.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(185.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .shimmer(shimmerProgress, state.isLoading),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.fullName ?: "Full Name Placeholder",
                color = colorResource(id = R.color.text_white),
                fontSize = 32.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(shimmerProgress, state.isLoading)
            )

            Text(
                text = user?.status?.name?.lowercase() ?: "offline",
                color = colorResource(id = user?.status?.color ?: R.color.inactive_grey),
                fontSize = 16.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(shimmerProgress, state.isLoading)
            )
        }
    }
}
