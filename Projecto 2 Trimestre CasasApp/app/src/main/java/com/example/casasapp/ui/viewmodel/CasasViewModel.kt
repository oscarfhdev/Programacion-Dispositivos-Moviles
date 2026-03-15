package com.example.casasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casasapp.data.Casa
import com.example.casasapp.data.RepositorioCasas
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// puente entre la vista y el Repositorio siguiendo MVVM estricto
class CasasViewModel(private val repositorio: RepositorioCasas) : ViewModel() {

    // pillamos todas las casas a través del repo
    val casas: StateFlow<List<Casa>> = repositorio.getAllCasas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // metemos una nueva a través del repo
    fun addCasa(casa: Casa) {
        viewModelScope.launch {
            repositorio.insertCasa(casa)
        }
    }

    // borramos a través del repo
    fun deleteCasa(casa: Casa) {
        viewModelScope.launch {
            repositorio.deleteCasa(casa)
        }
    }

    // editamos a través del repo
    fun updateCasa(casa: Casa) {
        viewModelScope.launch {
            repositorio.updateCasa(casa)
        }
    }

    // buscamos por id a través del repo
    fun getCasaById(id: Int): StateFlow<Casa?> {
        return repositorio.getCasaById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }
}
