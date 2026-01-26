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
            // Initialize the database and the ViewModel
            val database = AppDatabase.getDatabase(this)
            val viewModel: CasasViewModel = viewModel(factory = ViewModelFactory(database.casaDao()))
            CasasAppTheme {
                // The AppNavigation composable is the root of the app's UI
                AppNavigation(viewModel)
            }
        }
    }
}

/**
 * This composable defines the navigation graph of the application.
 * It uses a NavHost to define the different screens (destinations) and the routes to navigate between them.
 */
@Composable
fun AppNavigation(viewModel: CasasViewModel) {
    // The NavController is responsible for managing the navigation stack
    val navController = rememberNavController()
    // The NavHost is a container for the different screens
    NavHost(navController = navController, startDestination = "inicio") {
        // Each composable() block defines a screen and its route
        composable("inicio") {
            PantallaInicio(navController)
        }
        composable("galeria") {
            PantallaGaleria(navController, viewModel)
        }
        composable("formulario") {
            PantallaFormulario(navController, viewModel)
        }
        // This screen takes an argument (the house ID) to show the details of a specific house
        composable(
            "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }) 
        ) {
            // Placeholder for detail screen
        }
    }
}
