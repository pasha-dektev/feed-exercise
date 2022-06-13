package com.lightricks.feedexercise

import android.app.Application
import android.content.Context
import com.lightricks.feedexercise.di.AppComponent
import com.lightricks.feedexercise.di.DaggerAppComponent

internal class FeedApp : Application() {
    @Volatile
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}

internal fun Context.appComponent(): AppComponent {
    return (applicationContext as FeedApp).appComponent
}
