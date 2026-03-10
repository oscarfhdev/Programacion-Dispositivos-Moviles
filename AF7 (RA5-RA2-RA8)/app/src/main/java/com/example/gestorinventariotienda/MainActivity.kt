package com.example.gestorinventariotienda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gestorinventariotienda.ui.navigation.AppNavigation
import com.example.gestorinventariotienda.ui.theme.GestorInventarioTiendaTheme
import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint es obligatorio para las activities (y fragments) cuando inyectas 
// ViewModels con Hilt en Composes dentro de su contexto.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El tema visual de Material 3 generado por defecto
            GestorInventarioTiendaTheme {
                // Invocamos el andamiaje principal de la navegación
                AppNavigation()
            }
        }
    }
}