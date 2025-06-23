package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WelcomeUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : WelcomeUseCase {
    override suspend fun setWelcomeShown() {
        dataStoreRepository.setWelcomeShown()
    }

    override fun getWelcomeShown(): Flow<Boolean> {
        return dataStoreRepository.getWelcomeShown()
    }
}