package com.example.tasky.di

import android.app.Application
import com.example.tasky.common.data.PreferenceHelper
import com.example.tasky.common.data.SessionStateManagerManagerImpl
import com.example.tasky.common.data.remote.ApiKeyInterceptor
import com.example.tasky.common.data.remote.AuthInterceptor
import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.data.repository.TokenRemoteRepositoryImpl
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.EmailPatternValidator
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.domain.TokenManager
import com.example.tasky.common.domain.repository.TokenRemoteRepository
import com.example.tasky.common.domain.util.EmailPatternValidatorImpl
import com.example.tasky.feature_agenda.data.repository.AuthenticatedRemoteRepositoryImpl
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import com.example.tasky.feature_login.data.repository.UserRemoteRepositoryImpl
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionStateManager: SessionStateManager,
        tokenManager: TokenManager
    ): AuthInterceptor {
        return AuthInterceptor(sessionStateManager, tokenManager)
    }


    @Provides
    @Singleton
    fun provideTokenManager(
        tokenRepository: TokenRemoteRepository,
        sessionStateManager: SessionStateManager
    ): TokenManager {
        return TokenManager(tokenRepository, sessionStateManager)
    }

    @Provides
    @Singleton
    @DefaultClient
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAuthenticatedOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @TokenClient
    fun provideTokenOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @DefaultApi
    fun provideTaskyApi(@DefaultClient okHttpClient: OkHttpClient): TaskyApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskyApi::class.java)
    }

    @Provides
    @Singleton
    @AuthenticatedApi
    fun provideAuthenticatedTaskyApi(@AuthenticatedClient okHttpClient: OkHttpClient): TaskyApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskyApi::class.java)
    }

    @Provides
    @Singleton
    @TokenApi
    fun provideTokenApi(@TokenClient okHttpClient: OkHttpClient): TaskyApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteRepository(
        @DefaultApi api: TaskyApi,
        app: Application
    ): UserRemoteRepository {
        return UserRemoteRepositoryImpl(api, app)
    }

    @Provides
    fun providesEmailPatternValidator(): EmailPatternValidator {
        return EmailPatternValidatorImpl()
    }

    @Provides
    @Singleton
    fun provideSessionStateManager(
        appPreferences: PreferenceHelper
    ): SessionStateManager {
        return SessionStateManagerManagerImpl(appPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthenticatedRemoteRepository(
        @AuthenticatedApi api: TaskyApi
    ): AuthenticatedRemoteRepository {
        return AuthenticatedRemoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideTokenRemoteRepository(
        @TokenApi api: TaskyApi
    ): TokenRemoteRepository {
        return TokenRemoteRepositoryImpl(api)
    }
}