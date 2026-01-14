package com.sofamaniac.reboost.di

import android.content.Context
import com.sofamaniac.reboost.data.repository.AccountsRepository
import com.sofamaniac.reboost.data.repository.AccountsRepositoryImpl
import dagger.Binds
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
    fun providesAccountsRepository(
        @ApplicationContext context: Context
    ): AccountsRepositoryImpl {
        return AccountsRepositoryImpl(context)

    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountsModuleAbstract {

    @Binds
    @Singleton
    abstract fun bindsAccountsRepository(
        accountsRepositoryImpl: AccountsRepositoryImpl
    ): AccountsRepository
}
