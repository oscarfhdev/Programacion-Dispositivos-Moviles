package com.example.gestorinventariotienda.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para instanciar DataStore en el Context del dispositivo.
// Le damos como nombre al archivo "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Administrador de las preferencias (DataStore) para encapsular el acceso.
class SettingsManager(private val context: Context) {

    // Definimos las claves para guardar nuestras preferencias (nombre de la tienda y SSID wifi del local).
    companion object {
        val STORE_NAME_KEY = stringPreferencesKey("store_name")
        val STORE_WIFI_SSID_KEY = stringPreferencesKey("store_wifi_ssid")
    }

    // Leemos el nombre de la tienda en forma de Flow. Por defecto será "Mi Tienda".
    val storeNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[STORE_NAME_KEY] ?: "Mi Tienda"
    }

    // Leemos el SSID WiFi configurado. Por defecto dejaremos el string vacío.
    val storeWifiSsidFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[STORE_WIFI_SSID_KEY] ?: ""
    }

    // Función para actualizar el nombre de la tienda en DataStore.
    suspend fun saveStoreName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[STORE_NAME_KEY] = name
        }
    }

    // Función para guardar el SSID WiFi autorizado de la tienda.
    suspend fun saveStoreWifiSsid(ssid: String) {
        context.dataStore.edit { preferences ->
            preferences[STORE_WIFI_SSID_KEY] = ssid
        }
    }
}
