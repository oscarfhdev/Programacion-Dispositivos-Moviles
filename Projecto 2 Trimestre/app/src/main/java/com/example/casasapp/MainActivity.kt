package com.example.casasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.casasapp.data.AppDatabase
import com.example.casasapp.ui.pantallas.PantallaDetalle
import com.example.casasapp.ui.pantallas.PantallaFormulario
import com.example.casasapp.ui.pantallas.PantallaGaleria
import com.example.casasapp.ui.pantallas.PantallaInicio
import com.example.casasapp.ui.theme.CasasAppTheme
import com.example.casasapp.ui.viewmodel.CasasViewModel
import com.example.casasapp.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Inicializar la base de datos y el ViewModel
            val database = AppDatabase.getDatabase(this)
            val viewModel: CasasViewModel = viewModel(factory = ViewModelFactory(database.casaDao()))
            CasasAppTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

/**
 * Composable que define el grafo de navegación de la aplicación.
 * Usa un NavHost para definir las pantallas y las rutas entre ellas.
 */
@Composable
fun AppNavigation(viewModel: CasasViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio") {
        // Pantalla de inicio / landing
        composable("inicio") {
            PantallaInicio(navController)
        }
        // Galería con todas las viviendas
        composable("galeria") {
            PantallaGaleria(navController, viewModel)
        }
        // Formulario para añadir una nueva vivienda
        composable("formulario") {
            PantallaFormulario(navController, viewModel)
        }
        // Pantalla de detalle con el ID de la vivienda como argumento
        composable(
            "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val casaId = backStackEntry.arguments?.getInt("id") ?: 0
            PantallaDetalle(navController, viewModel, casaId)
        }
    }
}
