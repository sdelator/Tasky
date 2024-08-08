package com.example.tasky.feature_agenda.data.local

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    fun delete(event: EventEntity)

    @Query("DELETE FROM events")
    fun deleteAllEvents()
}
