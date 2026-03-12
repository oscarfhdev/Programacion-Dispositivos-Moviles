package com.example.casasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casasapp.data.Casa
import com.example.casasapp.data.CasaDAO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// hacemos de puente entre la vista y el DAO usando stateflows
class CasasViewModel(private val casaDao: CasaDAO) : ViewModel() {

    // pillamos todas las casas para enseñarlas
    val casas: StateFlow<List<Casa>> = casaDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // metemos una nueva en sqlite
    fun addCasa(casa: Casa) {
        viewModelScope.launch {
            casaDao.insert(casa)
        }
    }

    // para borrar por completo
    fun deleteCasa(casa: Casa) {
        viewModelScope.launch {
            casaDao.delete(casa)
        }
    }

    // sacamos una sola pasandole la id
    fun getCasaById(id: Int): StateFlow<Casa?> {
        return casaDao.getById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }
}
