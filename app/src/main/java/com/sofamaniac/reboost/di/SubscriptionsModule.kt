package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.ui.subredditList.SubscriptionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SubscriptionsModule {

    @Provides
    fun provideSubscriptionsRepository(api: RedditAPIService): SubscriptionsRepository {
        return SubscriptionsRepository(api)
    }

}
