package com.example.casasapp.data

import kotlinx.coroutines.flow.Flow

class RepositorioCasas(private val casaDao: CasaDAO) {

    // pillamos todas de golpe
    fun getAllCasas(): Flow<List<Casa>> {
        return casaDao.getAll()
    }

    // pillamos una sola pasando el id
    fun getCasaById(id: Int): Flow<Casa?> {
        return casaDao.getById(id)
    }

    // metemos una nueva
    suspend fun insertCasa(casa: Casa) {
        casaDao.insert(casa)
    }

    // borramos una casa
    suspend fun deleteCasa(casa: Casa) {
        casaDao.delete(casa)
    }

    // por si editamos en el futuro
    suspend fun updateCasa(casa: Casa) {
        casaDao.update(casa)
    }
}
