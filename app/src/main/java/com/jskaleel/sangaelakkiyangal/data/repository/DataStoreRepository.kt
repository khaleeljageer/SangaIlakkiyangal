package com.jskaleel.sangaelakkiyangal.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setWelcomeShown()
    fun getWelcomeShown(): Flow<Boolean>
}
