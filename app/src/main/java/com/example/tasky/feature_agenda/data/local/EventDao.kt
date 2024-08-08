package com.example.tasky.feature_agenda.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = (:id)")
    fun getAllEventsById(id: String): List<EventEntity>

    @Upsert
    fun upsert(event: EventEntity)

    @Query("DELETE FROM events WHERE id = (:id)")
    fun delete(id: String)

    @Query("DELETE FROM events")
    fun deleteAllEvents()
}
