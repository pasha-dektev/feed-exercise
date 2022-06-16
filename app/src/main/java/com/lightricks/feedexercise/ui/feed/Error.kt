package com.lightricks.feedexercise.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lightricks.feedexercise.R
import com.lightricks.feedexercise.ui.feed.model.ViewState

@Composable
internal fun ErrorState(
    state: ViewState.Error,
    onReload: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Text(text = stringResource(id = state.errorMessage))
            TextButton(onClick = onReload) {
                Text(text = stringResource(id = R.string.retry_button))
            }
        }
    }
}
