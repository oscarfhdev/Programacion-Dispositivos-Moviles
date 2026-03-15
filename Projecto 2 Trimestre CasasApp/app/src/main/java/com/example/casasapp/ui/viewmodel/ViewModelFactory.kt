package com.example.casasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.casasapp.data.RepositorioCasas

class ViewModelFactory(private val repositorio: RepositorioCasas) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CasasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CasasViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
