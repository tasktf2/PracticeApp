package com.setjy.practiceapp.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
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
                ZulipTheme {
                    val shimmerProgress = rememberShimmerProgress()

                    ProfileScreen(state = composeState, shimmerProgress)
                }
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
fun ProfileScreen(state: ProfileState, shimmerProgress: State<Float>) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val user = state.userItemUI

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = user?.avatarUrl,
                contentDescription = stringResource(R.string.cd_avatar),
                modifier = Modifier
                    .size(AppTheme.dimens.avatarBig)
                    .clip(RoundedCornerShape(AppTheme.dimens.marginDefault))
                    .shimmer(shimmerProgress, state.isLoading),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.marginDefault))

            Text(
                text = user?.fullName ?: stringResource(R.string.ph_full_name),
                style = AppTheme.typography.textHeader,
                modifier = Modifier
                    .clip(RoundedCornerShape(AppTheme.dimens.shapeMicro))
                    .shimmer(shimmerProgress, state.isLoading)
            )

            Text(
                text = user?.status?.name?.lowercase() ?: UserStatus.OFFLINE.name.lowercase(),
                color = user?.status?.colorX ?: AppTheme.colors.disabled,
                style = AppTheme.typography.textSmall,
                modifier = Modifier
                    .clip(RoundedCornerShape(AppTheme.dimens.shapeMicro))
                    .shimmer(shimmerProgress, state.isLoading)
            )
        }
    }
}