package com.example.tasky.feature_agenda.domain.repository

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.feature_agenda.domain.model.SyncAgendaResponse
import com.example.tasky.feature_agenda.presentation.AgendaItem

interface AgendaRemoteRepository {
    suspend fun loadAgenda(time: Long): Result<List<AgendaItem>, DataError.Network>
    suspend fun loadFullAgenda(): Result<List<AgendaItem>, DataError.Network>

    suspend fun syncAgenda(): Result<SyncAgendaResponse, DataError.Network>
}