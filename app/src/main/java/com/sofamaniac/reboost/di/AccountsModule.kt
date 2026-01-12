package com.sofamaniac.reboost.di

import android.content.Context
import com.sofamaniac.reboost.accounts.AccountsRepository
import com.sofamaniac.reboost.accounts.AccountsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountsModule {

    @Provides
    @Singleton
    fun provideAccountsRepository(
        @ApplicationContext context: Context
    ): AccountsRepository {
        return AccountsRepositoryImpl(context)
    }
}