package com.example.casasapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// aqui montamos la configuracion de la bd local
@Database(entities = [Casa::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // el enchufe al DAO
    abstract fun casaDao(): CasaDAO

    companion object {
        // ojito con los hilos
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // el singleton de toda la vida para que no haya 2 bds iguales rulando
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "casas_database"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
