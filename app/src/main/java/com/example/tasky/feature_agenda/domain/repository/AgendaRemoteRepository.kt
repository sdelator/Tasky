package com.example.tasky.feature_agenda.domain.repository

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError

interface AgendaRemoteRepository {
    suspend fun loadAgenda(time: Long): Result<List<AgendaItem>, DataError.Network>
    suspend fun loadFullAgenda(): Result<List<AgendaItem>, DataError.Network>

//    suspend fun syncAgenda(): Result<SyncAgendaResponse, DataError.Network>
}