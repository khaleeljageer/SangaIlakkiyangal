package com.jskaleel.sangaelakkiyangal.di

import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCaseImpl
import com.jskaleel.sangaelakkiyangal.domain.usecase.WelcomeUseCase
import com.jskaleel.sangaelakkiyangal.domain.usecase.WelcomeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun getWelcomeUseCase(
        useCase: WelcomeUseCaseImpl,
    ): WelcomeUseCase

    @Binds
    @Singleton
    abstract fun getBooksUseCase(
        useCase: BooksUseCaseImpl,
    ): BooksUseCase
}