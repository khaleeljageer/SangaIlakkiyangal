package com.jskaleel.sangaelakkiyangal.di

import com.jskaleel.sangaelakkiyangal.core.FileDownloader
import com.jskaleel.sangaelakkiyangal.core.FileDownloaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadModule {
    @Binds
    @Singleton
    abstract fun bindFileDownloader(
        fileDownloaderImpl: FileDownloaderImpl
    ): FileDownloader
}