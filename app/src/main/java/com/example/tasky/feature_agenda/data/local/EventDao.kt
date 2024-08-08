package com.example.tasky.feature_agenda.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): List<EventInfo>

    @Upsert
    fun upsert(event: EventInfo)

    @Delete
    fun delete(event: EventInfo)
}
