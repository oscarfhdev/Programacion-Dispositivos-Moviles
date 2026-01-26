package com.example.casasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.casasapp.data.CasaDAO

class ViewModelFactory(private val casaDao: CasaDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CasasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CasasViewModel(casaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
