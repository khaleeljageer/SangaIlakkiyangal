package com.jskaleel.sangaelakkiyangal

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SangamApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(60000)
            .setConnectTimeout(60000)
            .build()
        PRDownloader.initialize(applicationContext, config)
    }
}