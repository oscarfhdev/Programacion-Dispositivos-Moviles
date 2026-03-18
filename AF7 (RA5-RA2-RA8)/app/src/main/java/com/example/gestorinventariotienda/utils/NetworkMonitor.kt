package com.example.gestorinventariotienda.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Comprueba si el dispositivo tiene cualquier tipo de conexión a Internet (Datos, WiFi, etc.)
    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // Comprueba si está conectado SÓLO por Wi-Fi.
    fun isConnectedToWifi(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    // Valida si el SSID actual coincide con el guardado por la tienda.
    // NOTA LEGAL/TÉCNICA DE ANDROID:
    // A partir de Android 8.1+, para leer el SSID "real" (nombre de la red Wifi), Google obliga a
    // tener encendido el GPS (Ubicación) y tener los permisos de ACCESS_FINE_LOCATION. 
    // De lo contrario, devuelve "<unknown ssid>".
    // Para simplificar esta app (y que no rompa por permisos de locación), vamos a hacer una validación "blanda"
    // simulando el check si falla, pero dejamos la estructura lista.
    fun isConnectedToStoreWifi(savedSsid: String): Boolean {
        if (!isConnectedToWifi()) return false
        if (savedSsid.isBlank()) return false // Si no hay configurada ninguna, no pasamos
        
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifiManager.connectionInfo
        
        // El SSID de Android suele venir rodeado de comillas (ej: "MiRed")
        val currentSsid = info.ssid?.replace("\"", "") ?: ""
        
        // Si no podemos obtenerlo por falta de permisos de GPS en Android modernos, 
        // bloqueamos la descarga por seguridad, tal y como se solicita en los requisitos.
        if (currentSsid == "<unknown ssid>" || currentSsid.isBlank()) {
           return false 
        }

        return currentSsid == savedSsid
    }
}
