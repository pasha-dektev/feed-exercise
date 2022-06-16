package com.lightricks.feedexercise.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lightricks.feedexercise.data.utils.DirectExecutor
import com.lightricks.feedexercise.data.utils.test
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.database.FeedItemEntity
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
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FeedDatabaseTest {
    private lateinit var feedDao: FeedDao
    private lateinit var db: FeedDatabase

    private val dispatcher = UnconfinedTestDispatcher()

    private val testFeedItemTemplate = FeedItemEntity(id = "", thumbnailUrl = "thumbnailUrl", isPremium = false)

    @Before
    fun createDb() {
        Dispatchers.setMain(dispatcher)
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), FeedDatabase::class.java)
            .setQueryExecutor(DirectExecutor)
            .setTransactionExecutor(DirectExecutor)
            .build()
        feedDao = db.provideFeedDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        Dispatchers.resetMain()
    }

    @Test
    fun dao_saveFeed() = runTest(dispatcher) {
        val feed = createFeed(10)
        feedDao.saveFeed(feed = feed)

        feedDao.getFeed().test(this) {
            assertExactlyValues(feed)
        }
    }

    @Test
    fun dao_getFeedCount() = runTest(dispatcher) {
        val numberOfItems = 11
        val feed = createFeed(numberOfItems)
        feedDao.saveFeed(feed = feed)
        Assert.assertEquals(numberOfItems, feedDao.getFeedCount())
    }

    @Test
    fun dao_deleteAllFeedItems() = runTest(dispatcher) {
        val numberOfItems = 17
        val feed = createFeed(numberOfItems)
        feedDao.saveFeed(feed = feed)
        Assert.assertEquals(numberOfItems, feedDao.getFeedCount())
        feedDao.deleteAllFeedItems()
        Assert.assertEquals(0, feedDao.getFeedCount())
    }

    private fun createFeed(itemsCount: Int): List<FeedItemEntity> {
        return mutableListOf<FeedItemEntity>().apply {
            repeat(itemsCount) { index ->
                testFeedItemTemplate.copy(id = index.toString()).let(::add)
            }
        }
    }
}
