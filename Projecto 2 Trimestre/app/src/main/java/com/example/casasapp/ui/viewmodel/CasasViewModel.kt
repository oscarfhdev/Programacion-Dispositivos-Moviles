package com.example.casasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casasapp.data.Casa
import com.example.casasapp.data.CasaDAO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado de las casas.
 * Actúa como intermediario entre la capa de datos (Room) y la interfaz de usuario.
 */
class CasasViewModel(private val casaDao: CasaDAO) : ViewModel() {

    /** Lista reactiva de todas las casas en la base de datos */
    val casas: StateFlow<List<Casa>> = casaDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /** Inserta una nueva casa en la base de datos */
    fun addCasa(casa: Casa) {
        viewModelScope.launch {
            casaDao.insert(casa)
        }
    }

    /** Elimina una casa de la base de datos */
    fun deleteCasa(casa: Casa) {
        viewModelScope.launch {
            casaDao.delete(casa)
        }
    }

    /** Obtiene una casa por su ID como un Flow reactivo */
    fun getCasaById(id: Int): StateFlow<Casa?> {
        return casaDao.getById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }
}
