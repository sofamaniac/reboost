package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.feed.HomeRepository
import com.sofamaniac.reboost.domain.repository.feed.SavedRepository
import com.sofamaniac.reboost.domain.repository.feed.SubredditPostsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHomeRepository(api: RedditAPIService): HomeRepository {
        return HomeRepository(api)
    }

    @Provides
    fun provideSubredditPostsRepository(api: RedditAPIService): SubredditPostsRepository {
        return SubredditPostsRepository(api)
    }

    @Provides
    @Singleton
    fun provideSavedRepository(api: RedditAPIService, accountsRepository: AccountsRepository): SavedRepository {
        return SavedRepository(api, accountsRepository)
    }
}

