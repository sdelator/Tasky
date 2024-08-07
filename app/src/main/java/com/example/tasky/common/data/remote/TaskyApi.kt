package com.example.tasky.common.data.remote

import com.example.tasky.common.domain.model.AccessTokenResponse
import com.example.tasky.feature_agenda.data.model.AgendaResponse
import com.example.tasky.feature_agenda.data.model.AttendeeAccountDto
import com.example.tasky.feature_agenda.data.model.EventDto
import com.example.tasky.feature_agenda.data.model.ReminderDto
import com.example.tasky.feature_agenda.data.model.TaskDto
import com.example.tasky.feature_login.data.model.AccessToken
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    ): Response<EventDto>

    @DELETE("/event")
    suspend fun deleteEvent(@Query("eventId") eventId: String): Response<Unit>

    @GET("/event")
    suspend fun loadEvent(@Query("eventId") eventId: String): Response<EventDto>

    @Multipart
    @PUT("/event")
    suspend fun updateEvent(
        @Part("update_event_request") updateEventRequest: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): Response<EventDto>

    @GET("/attendee")
    suspend fun getAttendee(@Query("email") email: String): Response<AttendeeAccountDto>

    @DELETE("/attendee")
    suspend fun deleteAttendee(@Query("eventId") eventId: String): Response<Unit>

    @GET("/agenda")
    suspend fun loadAgenda(@Query("time") time: Long): Response<AgendaResponse>

//    @PUT("/syncAgenda")
//    suspend fun syncAgendaItem(): Response<SyncAgendaResponse>

    @GET("/fullAgenda")
    suspend fun loadFullAgenda(): Response<AgendaResponse>

    @POST("/task")
    suspend fun createTask(@Body taskDtoDetails: TaskDto): Response<Unit>

    @DELETE("/task")
    suspend fun deleteTask(@Query("taskId") taskId: String): Response<Unit>

    @GET("/task")
    suspend fun loadTask(@Query("taskId") taskId: String): Response<TaskDto>

    @PUT("/task")
    suspend fun updateTask(@Body taskDtoDetails: TaskDto): Response<Unit>

    @POST("/reminder")
    suspend fun createReminder(@Body reminderDtoDetails: ReminderDto): Response<Unit>

    @DELETE("/reminder")
    suspend fun deleteReminder(@Query("reminderId") reminderId: String): Response<Unit>

    @GET("/reminder")
    suspend fun loadReminder(@Query("reminderId") reminderId: String): Response<ReminderDto>

    @PUT("/reminder")
    suspend fun updateReminder(@Body reminderDtoDetails: ReminderDto): Response<Unit>
}