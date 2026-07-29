package com.setjy.practiceapp.presentation.ui.people.holder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import com.setjy.practiceapp.presentation.ui.profile.UserStatus
import com.setjy.practiceapp.presentation.ui.profile.colorX
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import com.setjy.practiceapp.util.rememberShimmerProgress
import com.setjy.practiceapp.util.shimmer

@Composable
fun UserItem(
    item: UserItemUI,
    isLoading: Boolean,
    progress: State<Float>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = item.avatarUrl,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.cd_avatar),
                modifier = Modifier
                    .size(AppTheme.dimens.avatarSmall)
                    .clip(CircleShape)
                    .shimmer(progress, isLoading),
                contentScale = ContentScale.Crop
            )

            UserStatus(item.status, progress, isLoading)
        }

        Column(
            modifier = Modifier.padding(start = AppTheme.dimens.marginMedium)
        ) {
            Text(
                modifier = Modifier.shimmer(progress, isLoading),
                fontSize = AppTheme.typography.large,
                text = item.fullName,
                color = AppTheme.colors.textPrimary
            )
            Text(
                modifier = Modifier.shimmer(progress, isLoading),
                fontSize = AppTheme.typography.micro,
                text = item.userEmail,
                color = AppTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun BoxScope.UserStatus(
    status: UserStatus,
    progress: State<Float>,
    isLoading: Boolean
) {
    Spacer(
        modifier = Modifier
            .size(AppTheme.dimens.status)
            .align(Alignment.BottomEnd)
            .background(AppTheme.colors.background, CircleShape)
            .padding(AppTheme.dimens.statusBorder)
            .background(status.colorX, CircleShape)
            .shimmer(progress, isLoading),

        )
}

@Preview
@Composable
private fun UserItemPreview() {
    ZulipTheme {

        UserItem(
            item = UserItemUI(
                userId = 1,
                avatarUrl = "",
                fullName = "fullName",
                userEmail = "userEmail",
                status = UserStatus.ACTIVE,
            ),
            isLoading = false,
            progress = rememberShimmerProgress()
        )
    }
}
