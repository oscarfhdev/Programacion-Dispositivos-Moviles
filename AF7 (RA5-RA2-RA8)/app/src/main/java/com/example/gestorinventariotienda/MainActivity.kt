package com.example.gestorinventariotienda

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
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
            // Solicitamos los permisos sensibles en tiempo de ejecución (obligatorio desde Android 6.0+)
            val permissionsToRequest = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            // En Android 13+ (API 33), el envío de notificaciones locales requiere permiso expreso
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // Aquí podríamos reaccionar si rechazan alguno. 
                // De momento solo provocamos el diálogo del sistema operativo y confiamos.
            }

            // Lanzamos el diálogo la primera vez que se monta la UI principal
            LaunchedEffect(Unit) {
                permissionLauncher.launch(permissionsToRequest.toTypedArray())
            }

            // El tema visual de Material 3 generado por defecto
            GestorInventarioTiendaTheme {
                // Invocamos el andamiaje principal de la navegación
                AppNavigation()
            }
        }
    }
}