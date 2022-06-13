package com.lightricks.feedexercise.ui.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.lightricks.feedexercise.R
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.ui.feed.model.ViewState
import kotlinx.coroutines.launch

@Composable
internal fun ContentState(
    content: ViewState.Content,
    onSwipeToRefresh: () -> Unit
) {
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = content.isReloading),
        onRefresh = onSwipeToRefresh
    ) {
        when (content.feed.isEmpty()) {
            true -> NoContent()
            false -> FeedGrid(content)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackbarHostState) {
        Snackbar(elevation = 16.dp, snackbarData = it)
    }
    if (content.errorMessage != null) {
        val errorText = stringResource(id = content.errorMessage)
        LaunchedEffect(key1 = content.errorMessage) {
            launch { snackbarHostState.showSnackbar(errorText) }
        }
    } else {
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}

@Composable
private fun FeedGrid(content: ViewState.Content) {
    LazyVerticalGrid(
        modifier = Modifier.padding(4.dp),
        columns = GridCells.Fixed(2),
    ) {
        items(content.feed) { item ->
            FeedItem(item)
        }
    }
}

@Composable
private fun NoContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.feed_empty_text),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FeedItem(item: FeedItem) {
    Card(modifier = Modifier.padding(4.dp), shape = RoundedCornerShape(6.dp)) {
        ThumbnailImage(item)
    }
}

@Composable
private fun ThumbnailImage(item: FeedItem) {
    AsyncImage(
        model = item.thumbnailUrl,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.ic_image_placeholder),
        fallback = painterResource(id = R.drawable.ic_image_placeholder),
        alignment = Alignment.Center
    )
}
