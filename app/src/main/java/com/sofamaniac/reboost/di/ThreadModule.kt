package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.repository.ThreadRepository
import com.sofamaniac.reboost.domain.repository.ThreadRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ThreadModule {

    @Provides
    fun provideThreadRepository(api: RedditAPIService): ThreadRepository {
        return ThreadRepositoryImpl(api)
    }

}