package com.example.cineapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pelicula::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun peliculaDao(): PeliculaDAO

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun obtenerBaseDatos(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "cineapp_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            }
        }
    }
}
