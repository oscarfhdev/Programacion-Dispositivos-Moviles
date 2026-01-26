package com.example.casasapp.data

import androidx.compose.runtime.mutableStateListOf

object RepositorioCasas {
    private val casas = mutableStateListOf(
        Casa(1, "Casa de campo", "", "Una bonita casa en el campo"),
        Casa(2, "Piso en la ciudad", "", "Un piso moderno en el centro de la ciudad"),
        Casa(3, "Chalet en la playa", "", "Un chalet con vistas al mar")
    )

    fun getCasas() = casas

    fun findCasaById(id: Int) = casas.find { it.id == id }

    fun addCasa(casa: Casa) {
        val newId = (casas.maxOfOrNull { it.id } ?: 0) + 1
        casas.add(casa.copy(id = newId))
    }
}
