package com.example.tasky.di

import android.app.Application
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.EmailPatternValidator
import com.example.tasky.common.domain.util.EmailPatternValidatorImpl
import com.example.tasky.feature_login.data.remote.ApiKeyInterceptor
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.data.repository.UserRemoteRemoteRepositoryImpl
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskyApi(okHttpClient: OkHttpClient): TaskyApi {
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
        api: TaskyApi,
        app: Application
    ): UserRemoteRepository {
        return UserRemoteRemoteRepositoryImpl(api, app)
    }

    @Provides
    fun providesEmailPatternValidator(): EmailPatternValidator {
        return EmailPatternValidatorImpl()
    }
}