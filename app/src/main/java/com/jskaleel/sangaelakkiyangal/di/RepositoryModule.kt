package com.jskaleel.sangaelakkiyangal.di

import com.jskaleel.sangaelakkiyangal.data.repository.DataStoreRepository
import com.jskaleel.sangaelakkiyangal.data.repository.DataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(
        repository: DataStoreRepositoryImpl
    ): DataStoreRepository
}