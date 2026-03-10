package com.example.gestorinventariotienda.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// DAO para manejar el buzón de incidencias
@Dao
interface IncidentDao {
    
    // Insertamos una nueva incidencia.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentReportEntity)

    // Obtenemos todas las incidencias, ordenadas por la más reciente primero (DESC).
    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentReportEntity>>
}
