package com.example.gestorinventariotienda.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Definimos la base de datos, indicando qué entidades contiene y la versión.
@Database(
    entities = [ProductEntity::class, IncidentReportEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    // Proveemos el acceso a los DAOs
    abstract fun productDao(): ProductDao
    abstract fun incidentDao(): IncidentDao
}
