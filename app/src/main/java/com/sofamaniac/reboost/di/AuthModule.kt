package com.sofamaniac.reboost.di

import android.content.Context
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.data.remote.api.auth.AuthConfig
import com.sofamaniac.reboost.data.remote.api.auth.BasicAuthClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthorizationService(@ApplicationContext context: Context): AuthorizationService {
       return AuthorizationService(context)
    }

    @Provides
    @Singleton
    fun provideAuthConfig(): AuthConfig {
        return AuthConfig()
    }

    @Provides
    fun provideClientAuth(): ClientAuthentication {
        return BasicAuthClient(BuildConfig.REDDIT_CLIENT_ID)
    }

}