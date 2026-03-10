package com.example.gestorinventariotienda.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad para guardar las incidencias del buzón de errores.
@Entity(tableName = "incidents")
data class IncidentReportEntity(
    // En este caso, el ID sí se genera automáticamente cada vez que insertamos uno nuevo.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    // Guardamos el momento exacto en el que ocurrió la incidencia.
    val timestamp: Long = System.currentTimeMillis()
)
