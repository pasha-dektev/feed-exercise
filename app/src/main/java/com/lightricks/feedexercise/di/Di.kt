package com.lightricks.feedexercise.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.data.FeedRepositoryImpl
import com.lightricks.feedexercise.data.local.LocalFeedDataSource
import com.lightricks.feedexercise.data.local.LocalFeedDataStore
import com.lightricks.feedexercise.data.local.RoomFeedDataSource
import com.lightricks.feedexercise.data.remote.RemoteFeedDataSource
import com.lightricks.feedexercise.data.remote.RetrofitFeedDataSource
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.domain.FeedEffectHandler
import com.lightricks.feedexercise.domain.FeedRedux.Effect
import com.lightricks.feedexercise.domain.FeedRedux.Message
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.RetrofitBaseUrlProvider
import com.lightricks.feedexercise.network.ThumbnailImageBaseUrlProvider
import com.lightricks.feedexercise.network.UrlProvider
import com.lightricks.feedexercise.network.interceptor.LoggingInterceptor
import com.lightricks.feedexercise.ui.MainActivity
import com.lightricks.feedexercise.util.log.AndroidLogger
import com.lightricks.feedexercise.util.log.Logger
import com.lightricks.feedexercise.util.mvi.CoroutineEffectHandler
import com.lightricks.feedexercise.util.mvi.CoroutineMessageEmitter
import dagger.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Component(modules = [FeedModule::class])
@Singleton
internal interface AppComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}

@Module
internal interface FeedBindModule {
    @Binds
    fun bindRetrofitFeedDataSource(
        retrofitFeedDataSource: RetrofitFeedDataSource
    ): RemoteFeedDataSource

    @Binds
    fun bindRoomDataSource(
        roomFeedDataSource: RoomFeedDataSource
    ): LocalFeedDataSource

    @Binds
    fun bindRoomDataStore(
        roomFeedDataSource: RoomFeedDataSource
    ): LocalFeedDataStore

    @Binds
    fun bindFeedRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository

    @Binds
    fun bindCoroutineEffectHandler(
        feedEffectHandler: FeedEffectHandler
    ): CoroutineEffectHandler<Effect, Message>

    @Binds
    fun bindCoroutineMessageEmitter(
        feedEffectHandler: FeedEffectHandler
    ): CoroutineMessageEmitter<Message>

    @Binds
    fun bindLogger(
        logger: AndroidLogger
    ): Logger
}

@Module(includes = [NetworkModule::class, FeedBindModule::class])
internal class FeedModule {

    @Provides
    @Singleton
    fun provideDataBase(context: Context): FeedDatabase {
        return Room.databaseBuilder(context, FeedDatabase::class.java, "db").build()
    }

    @Provides
    @Singleton
    fun provideFeedDao(db: FeedDatabase): FeedDao {
        return db.provideFeedDao()
    }
}

@Module(includes = [NetworkBindModule::class])
internal class NetworkModule {

    @Provides
    fun provideHttpClient(
        @Named("Logging")
        loggingInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("RetrofitBaseUrl")
        urlProvider: UrlProvider,
        okHttpClient: OkHttpClient,
        @Named("Json")
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlProvider.url)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideFeedApiService(
        retrofit: Retrofit
    ): FeedApiService {
        return retrofit.create()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Named("Json")
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }
}

@Module
internal interface NetworkBindModule {

    @Binds
    @Named("Logging")
    fun bindLoggingInterceptor(
        loggingInterceptor: LoggingInterceptor
    ): Interceptor

    @Binds
    @Named("RetrofitBaseUrl")
    fun bindRetrofitBaseUrlProvider(
        retrofitBaseUrlProvider: RetrofitBaseUrlProvider
    ): UrlProvider

    @Binds
    @Named("ThumbnailImageBaseUrl")
    fun bindThumbnailImageBaseUrlProvider(
        thumbnailImageBaseUrlProvider: ThumbnailImageBaseUrlProvider
    ): UrlProvider
}
