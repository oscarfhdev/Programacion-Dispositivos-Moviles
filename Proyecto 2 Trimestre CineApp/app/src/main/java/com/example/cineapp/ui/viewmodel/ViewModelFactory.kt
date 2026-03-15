package com.example.cineapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cineapp.data.RepositorioPeliculas

class ViewModelFactory(private val repositorioPeliculas: RepositorioPeliculas) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CineViewModel(repositorioPeliculas) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
