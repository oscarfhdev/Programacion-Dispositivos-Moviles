package com.example.casasapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * This is the main access point to the database. It is an abstract class that extends RoomDatabase.
 * The @Database annotation marks it as a Room database and specifies the entities it contains and the database version.
 */
@Database(entities = [Casa::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * This abstract method returns the DAO for the Casa entity.
     * Room will generate the implementation of this method.
     */
    abstract fun casaDao(): CasaDAO

    companion object {
        // The @Volatile annotation ensures that the INSTANCE variable is always up-to-date
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * This method returns a singleton instance of the database.
         * This is to prevent having multiple instances of the database open at the same time.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "casas_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
