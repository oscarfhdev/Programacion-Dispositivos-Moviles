package com.example.gestorinventariotienda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorinventariotienda.data.local.IncidentReportEntity
import com.example.gestorinventariotienda.data.preferences.SettingsManager
import com.example.gestorinventariotienda.domain.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: InventoryRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    // Flujo para la lista de incidencias
    val incidentsFlow: StateFlow<List<IncidentReportEntity>> = repository.getAllIncidents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Flujos de DataStore expuestos a Compose
    val storeName: StateFlow<String> = settingsManager.storeNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
        
    val storeWifiSsid: StateFlow<String> = settingsManager.storeWifiSsidFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    // Guardar nombre de tienda en DataStore
    fun updateStoreName(name: String) {
        viewModelScope.launch {
            settingsManager.saveStoreName(name)
        }
    }

    // Guardar SSID en DataStore
    fun updateWifiSsid(ssid: String) {
        viewModelScope.launch {
            settingsManager.saveStoreWifiSsid(ssid)
        }
    }

    // Agregar incidencia a Room
    fun addIncident(description: String) {
        viewModelScope.launch {
            repository.reportIncident(description)
        }
    }
}
