package com.lightricks.feedexercise.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lightricks.feedexercise.appComponent
import com.lightricks.feedexercise.ui.feed.*
import com.lightricks.feedexercise.ui.feed.model.ViewState
import javax.inject.Inject

/**
 * This is the main entry point into the app. This activity shows the [FeedFragment].
 */
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var feedViewModelFactory: FeedViewModelFactory

    private val viewModel: FeedViewModel by lazy {
        ViewModelProvider(this, feedViewModelFactory)[FeedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FeedScreen(viewModel)
            }
        }
    }

    @Composable
    private fun FeedScreen(viewModel: FeedViewModel) {
        val viewState = viewModel.viewState.collectAsState()
        when (val state = viewState.value) {
            ViewState.Loading -> LoadingState()
            is ViewState.Error -> ErrorState(state, viewModel::onRefresh)
            is ViewState.Content -> ContentState(content = state, onSwipeToRefresh = viewModel::onRefresh)
        }
    }
}
