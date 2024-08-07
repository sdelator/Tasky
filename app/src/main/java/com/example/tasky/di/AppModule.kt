package com.example.tasky.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.room.Room.databaseBuilder
import com.example.tasky.agenda_details.data.ImageCompressorImpl
import com.example.tasky.agenda_details.data.repository.AgendaDetailsRemoteRepositoryImpl
import com.example.tasky.agenda_details.domain.ImageCompressor
import com.example.tasky.agenda_details.domain.repository.AgendaDetailsRemoteRepository
import com.example.tasky.common.data.PreferenceHelper
import com.example.tasky.common.data.SessionStateManagerImpl
import com.example.tasky.common.data.notification_service.NotificationHandlerImpl
import com.example.tasky.common.data.remote.ApiKeyInterceptor
import com.example.tasky.common.data.remote.AuthInterceptor
import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.data.repository.TokenRemoteRepositoryImpl
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.EmailPatternValidator
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.domain.TokenManager
import com.example.tasky.common.domain.notification.NotificationHandler
import com.example.tasky.common.domain.repository.TokenRemoteRepository
import com.example.tasky.common.domain.util.EmailPatternValidatorImpl
import com.example.tasky.feature_agenda.data.local.EventDao
import com.example.tasky.feature_agenda.data.local.EventLocalRepositoryImpl
import com.example.tasky.feature_agenda.data.local.ReminderDao
import com.example.tasky.feature_agenda.data.local.ReminderLocalRepositoryImpl
import com.example.tasky.feature_agenda.data.local.TaskDao
import com.example.tasky.feature_agenda.data.local.TaskLocalRepositoryImpl
import com.example.tasky.feature_agenda.data.local.TaskyDatabase
import com.example.tasky.feature_agenda.data.repository.AgendaRemoteRepositoryImpl
import com.example.tasky.feature_agenda.data.repository.AuthenticatedRemoteRepositoryImpl
import com.example.tasky.feature_agenda.domain.local.EventLocalRepository
import com.example.tasky.feature_agenda.domain.local.ReminderLocalRepository
import com.example.tasky.feature_agenda.domain.local.TaskLocalRepository
import com.example.tasky.feature_agenda.domain.repository.AgendaRemoteRepository
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import com.example.tasky.feature_login.data.repository.UserRemoteRepositoryImpl
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Singleton
    fun providesAgendaRemoteRepository(
        @AuthenticatedApi api: TaskyApi
    ): AgendaRemoteRepository {
        return AgendaRemoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providesAgendaDetailsRemoteRepository(
        @AuthenticatedApi api: TaskyApi,
        contentResolver: ContentResolver
    ): AgendaDetailsRemoteRepository {
        return AgendaDetailsRemoteRepositoryImpl(api, contentResolver = contentResolver)
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
        return SessionStateManagerImpl(appPreferences)
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

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideImageCompressor(contentResolver: ContentResolver): ImageCompressor {
        return ImageCompressorImpl(contentResolver = contentResolver)
    }

    @Provides
    @Singleton
    fun provideNotificationHandler(@ApplicationContext context: Context): NotificationHandler {
        return NotificationHandlerImpl(context)
    }

    @Provides
    @Singleton
    fun provideTaskyDatabase(@ApplicationContext appContext: Context): TaskyDatabase {
        return databaseBuilder(
            appContext,
            TaskyDatabase::class.java,
            "tasky_database"
        ).build()
    }

    @Provides
    fun provideEventDao(taskyDatabase: TaskyDatabase): EventDao = taskyDatabase.eventDao()

    @Provides
    fun provideTaskDao(taskyDatabase: TaskyDatabase): TaskDao = taskyDatabase.taskDao()

    @Provides
    fun provideReminderDao(taskyDatabase: TaskyDatabase): ReminderDao = taskyDatabase.reminderDao()


    @Provides
    @Singleton
    fun provideEventLocalRepository(eventDao: EventDao): EventLocalRepository {
        return EventLocalRepositoryImpl(eventDao)
    }

    @Provides
    @Singleton
    fun provideTaskLocalRepository(taskDao: TaskDao): TaskLocalRepository {
        return TaskLocalRepositoryImpl(taskDao)
    }

    @Provides
    @Singleton
    fun provideReminderLocalRepository(reminderDao: ReminderDao): ReminderLocalRepository {
        return ReminderLocalRepositoryImpl(reminderDao)
    }
}