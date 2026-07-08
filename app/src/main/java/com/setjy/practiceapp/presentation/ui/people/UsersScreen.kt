package com.setjy.practiceapp.presentation.ui.people

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.ui.people.holder.UserItem
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import com.setjy.practiceapp.presentation.ui.profile.UserStatus
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import com.setjy.practiceapp.util.rememberShimmerProgress

@Composable
fun UsersScreen(
    state: PeopleState,
    onValueChange: (String) -> Unit,
    progress: State<Float>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        Search(
            onValueChange = onValueChange,
            search = state.search,
            placeholderText = stringResource(R.string.ph_users)
        )

        LazyColumn(
            contentPadding = PaddingValues(all = AppTheme.dimens.marginDefault),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.marginSmall)
        ) {

            val users = if (state.search.isNotBlank()) state.visibleUsers else state.users

            items(users.orEmpty(), key = { it.userId }) { user ->
                UserItem(
                    isLoading = state.isLoading,
                    item = user,
                    progress = progress,
                )
            }
        }
    }
}

@Composable
fun Search(
    onValueChange: (String) -> Unit,
    search: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier
) {
    Row {
        Spacer(
            modifier
                .background(AppTheme.colors.surface)
                .size(width = AppTheme.dimens.searchGap, height = AppTheme.dimens.search)
        )
        TextField(
            value = search,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.search),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = AppTheme.typography.larger
            ),
            placeholder = {
                Text(
                    text = placeholderText,
                    color = AppTheme.colors.textPrimary,
                    fontSize = AppTheme.typography.larger
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_vector_search_24),
                    contentDescription = stringResource(R.string.cd_search_icon),
                    tint = Color.Unspecified
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppTheme.colors.surface,
                unfocusedContainerColor = AppTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun UsersScreenPreview() {
    val mockUsers = List(10) { index ->
        UserItemUI(
            userId = index,
            fullName = "User Full Name $index",
            userEmail = "user$index@example.com",
            avatarUrl = "https://example.com/avatar$index.png",
            status = if (index % 2 == 0) UserStatus.ACTIVE else UserStatus.OFFLINE
        )
    }

    var search by remember { mutableStateOf("") }
    val progress = rememberShimmerProgress()

    ZulipTheme() {

        UsersScreen(
            state = PeopleState(users = mockUsers),
            onValueChange = { search = it },
            progress = progress
        )
    }
}
