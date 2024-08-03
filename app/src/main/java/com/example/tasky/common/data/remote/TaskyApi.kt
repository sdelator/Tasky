package com.example.tasky.common.data.remote

import com.example.tasky.agenda_details.data.model.EventResponse
import com.example.tasky.agenda_details.data.model.TaskResponse
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.domain.model.AccessTokenResponse
import com.example.tasky.feature_agenda.domain.model.AgendaResponse
import com.example.tasky.feature_agenda.domain.model.SyncAgendaResponse
import com.example.tasky.feature_login.data.model.AccessToken
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface TaskyApi {
    @POST("/register")
    suspend fun registerUser(
        @Body registerUserInfo: RegisterUserInfo
    ): Response<Unit>

    @POST("/login")
    suspend fun loginUser(
        @Body loginUserInfo: LoginUserInfo
    ): Response<LoginUserResponse>

    @POST("/accessToken")
    suspend fun refreshSession(@Body accessToken: AccessToken): Response<AccessTokenResponse>

    @GET("/authenticate")
    suspend fun checkAuthentication(): Response<Unit>

    @GET("/logout")
    suspend fun logout(): Response<Unit>

    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): Response<EventResponse>

    @GET("/agenda")
    suspend fun loadAgenda(@Query("time") time: Long): Response<AgendaResponse>

    @POST("/syncAgenda")
    suspend fun syncAgenda(): Response<SyncAgendaResponse>

//    @PUT("/syncAgenda")
//    suspend fun syncAgendaItem(): Response<SyncAgendaResponse>

    @GET("/fullAgenda")
    suspend fun loadFullAgenda(): Response<AgendaResponse>

    @POST("/task")
    suspend fun createTask(@Body taskDetails: AgendaItem.Task): Response<Unit>

    @PUT("/task")
    suspend fun updateTask(@Body taskDetails: AgendaItem.Task): Response<TaskResponse>

    @GET("/task")
    suspend fun loadTask(@Query("id") id: String): Response<TaskResponse>

    @POST("/reminder")
    suspend fun createReminder(@Body reminderDetails: AgendaItem.Reminder): Response<Unit>
}