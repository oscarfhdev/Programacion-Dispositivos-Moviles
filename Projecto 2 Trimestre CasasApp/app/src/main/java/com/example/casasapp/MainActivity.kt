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
import com.example.casasapp.data.RepositorioCasas
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
            // instanciamos la bbdd, el repo y el viewmodel para pasarle los datos a las pantallas
            val database = AppDatabase.getDatabase(this)
            val repositorio = RepositorioCasas(database.casaDao())
            val viewModel: CasasViewModel = viewModel(factory = ViewModelFactory(repositorio))
            CasasAppTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

// aqui montamos todo el tema de las rutas para movernos entre pantallas
@Composable
fun AppNavigation(viewModel: CasasViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio") {
        // la primera pantallita
        composable("inicio") {
            PantallaInicio(navController)
        }
        // pilla todas con un LazyColumn
        composable("galeria") {
            PantallaGaleria(navController, viewModel)
        }
        // el añadir de toda la vida o editar si viene con id
        composable(
            route = "formulario?idCasa={idCasa}",
            arguments = listOf(navArgument("idCasa") { 
                type = NavType.IntType
                defaultValue = -1 
            })
        ) { backStackEntry ->
            val idCasa = backStackEntry.arguments?.getInt("idCasa")?.takeIf { it != -1 }
            PantallaFormulario(navController, viewModel, idCasa)
        }
        // le pasamos la id de la casa pa hacer magia y enseñarla solita
        composable(
            "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val casaId = backStackEntry.arguments?.getInt("id") ?: 0
            PantallaDetalle(navController, viewModel, casaId)
        }
    }
}
