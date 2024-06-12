package com.example.tasky.di

import android.app.Application
import com.example.tasky.common.domain.Constants
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.data.repository.UserRemoteRemoteRepositoryImpl
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskyApi(): TaskyApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(TaskyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: TaskyApi, app: Application): UserRemoteRepository {
        return UserRemoteRemoteRepositoryImpl(api, app)
    }
}