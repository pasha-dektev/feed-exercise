@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lightricks.feedexercise.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lightricks.feedexercise.data.local.RoomFeedDataSource
import com.lightricks.feedexercise.data.remote.RetrofitFeedDataSource
import com.lightricks.feedexercise.data.utils.DirectExecutor
import com.lightricks.feedexercise.data.utils.test
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.domain.FeedError
import com.lightricks.feedexercise.network.Feed
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.Template
import com.lightricks.feedexercise.network.UrlProvider
import com.lightricks.feedexercise.util.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.net.UnknownHostException

@RunWith(AndroidJUnit4::class)
class FeedRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var db: FeedDatabase
    private lateinit var feedDao: FeedDao

    private val testFeedResponse = Feed(
        templates = mutableListOf<Template>().apply {
            repeat(10) { index ->
                Template(id = index.toString(), isPremium = false, thumbnailUrl = "thumbnailUrl").let(::add)
            }
        }
    )

    private val testThumbnailBaseUrlProvider = object : UrlProvider {
        override val url: String = ""
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), FeedDatabase::class.java)
            .setQueryExecutor(DirectExecutor)
            .setTransactionExecutor(DirectExecutor)
            .build()
        feedDao = db.provideFeedDao()
    }

    @After
    fun tearDown() {
        db.close()
        Dispatchers.resetMain()
    }

    @Test
    fun repo_refreshFeed_success() = runTest(dispatcher) {
        val roomFeedDataSource = RoomFeedDataSource(feedDao)
        val repository = FeedRepositoryImpl(
            localFeedDataStore = roomFeedDataSource,
            localFeedDataSource = roomFeedDataSource,
            remoteFeedDataSource = RetrofitFeedDataSource(
                apiService = object : FeedApiService {
                    override suspend fun getFeed(): Response<Feed> {
                        return Response.success(testFeedResponse)
                    }
                },
                thumbnailBaseUrlProvider = testThumbnailBaseUrlProvider
            )
        )

        Assert.assertTrue(repository.refresh() is Result.Success)
        repository.getFeed().test(this) {
            assertExactlyValues(testFeedResponse.toFeed(testThumbnailBaseUrlProvider))
        }
    }

    @Test
    fun repo_refreshFeed_networkError() = runTest(dispatcher) {
        val roomFeedDataSource = RoomFeedDataSource(feedDao)
        val repository = FeedRepositoryImpl(
            localFeedDataStore = roomFeedDataSource,
            localFeedDataSource = roomFeedDataSource,
            remoteFeedDataSource = RetrofitFeedDataSource(
                apiService = object : FeedApiService {
                    override suspend fun getFeed(): Response<Feed> {
                        throw UnknownHostException()
                    }
                },
                thumbnailBaseUrlProvider = testThumbnailBaseUrlProvider
            )
        )

        val result = repository.refresh()
        Assert.assertTrue(result is Result.Failure && result.error == FeedError.NetworkError)
    }

    @Test
    fun repo_refreshFeed_unknownError() = runTest(dispatcher) {
        val roomFeedDataSource = RoomFeedDataSource(feedDao)
        val repository = FeedRepositoryImpl(
            localFeedDataStore = roomFeedDataSource,
            localFeedDataSource = roomFeedDataSource,
            remoteFeedDataSource = RetrofitFeedDataSource(
                apiService = object : FeedApiService {
                    override suspend fun getFeed(): Response<Feed> {
                        throw RuntimeException()
                    }
                },
                thumbnailBaseUrlProvider = testThumbnailBaseUrlProvider
            )
        )

        val result = repository.refresh()
        Assert.assertTrue(result is Result.Failure && result.error == FeedError.UnknownError)
    }
}
