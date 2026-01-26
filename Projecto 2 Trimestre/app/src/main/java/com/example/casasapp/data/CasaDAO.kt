package com.example.casasapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * This is the Data Access Object (DAO) for the Casa entity.
 * It is an interface that defines the methods for interacting with the database.
 * Room will generate the implementation of this interface at compile time.
 */
@Dao
interface CasaDAO {
    /**
     * This method retrieves all the houses from the database.
     * It returns a Flow, which is a stream of data that can be observed for changes.
     */
    @Query("SELECT * FROM casas")
    fun getAll(): Flow<List<Casa>>

    /**
     * This method inserts a new house into the database.
     * The @Insert annotation tells Room to generate the corresponding SQL INSERT statement.
     * The suspend modifier indicates that this is a coroutine function that should be called from a coroutine scope.
     */
    @Insert
    suspend fun insert(casa: Casa)

    /**
     * This method deletes a house from the database.
     * The @Delete annotation tells Room to generate the corresponding SQL DELETE statement.
     */
    @Delete
    suspend fun delete(casa: Casa)
}
