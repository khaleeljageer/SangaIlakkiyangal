package com.jskaleel.sangaelakkiyangal.domain.usecase

import kotlinx.coroutines.flow.Flow

interface WelcomeUseCase {
    suspend fun setWelcomeShown()
    fun getWelcomeShown(): Flow<Boolean>
}
