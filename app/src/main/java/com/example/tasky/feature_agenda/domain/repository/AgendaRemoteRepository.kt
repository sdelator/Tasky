package com.example.tasky.feature_agenda.domain.repository

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.feature_agenda.domain.model.AgendaResponse
import com.example.tasky.feature_agenda.domain.model.SyncAgendaResponse

interface AgendaRemoteRepository {
    suspend fun loadAgenda(time: Long): Result<AgendaResponse, DataError.Network>
    suspend fun loadFullAgenda(): Result<AgendaResponse, DataError.Network>

    suspend fun syncAgenda(): Result<SyncAgendaResponse, DataError.Network>
}