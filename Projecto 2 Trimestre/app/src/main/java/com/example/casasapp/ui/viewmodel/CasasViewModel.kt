package com.example.casasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casasapp.data.Casa
import com.example.casasapp.data.CasaDAO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CasasViewModel(private val casaDao: CasaDAO) : ViewModel() {
    val casas = casaDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addCasa(casa: Casa) {
        viewModelScope.launch {
            casaDao.insert(casa)
        }
    }
}
