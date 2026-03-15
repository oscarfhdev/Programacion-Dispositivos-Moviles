package com.example.cineapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cineapp.data.AppDatabase
import com.example.cineapp.data.RepositorioPeliculas
import com.example.cineapp.ui.pantallas.PantallaDetalle
import com.example.cineapp.ui.pantallas.PantallaFormulario
import com.example.cineapp.ui.pantallas.PantallaGaleria
import com.example.cineapp.ui.pantallas.PantallaInicio
import com.example.cineapp.ui.theme.CineAppTheme
import com.example.cineapp.ui.viewmodel.CineViewModel
import com.example.cineapp.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialidades Room, Repository y ViewModelFactory
        val database = AppDatabase.obtenerBaseDatos(this)
        val repositorio = RepositorioPeliculas(database.peliculaDao())
        val factory = ViewModelFactory(repositorio)
        viewModel = ViewModelProvider(this, factory)[CineViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            CineAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "inicio") {
                        
                        composable("inicio") {
                            PantallaInicio(
                                onEntrarClick = {
                                    navController.navigate("galeria") {
                                        popUpTo("inicio") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("galeria") {
                            PantallaGaleria(
                                viewModel = viewModel,
                                onNuevaPelicula = { navController.navigate("formulario?idPelicula=0") },
                                onEditarPelicula = { id -> navController.navigate("formulario?idPelicula=$id") },
                                onVerDetalle = { id -> navController.navigate("detalle/$id") }
                            )
                        }

                        composable(
                            route = "formulario?idPelicula={idPelicula}",
                            arguments = listOf(navArgument("idPelicula") { 
                                type = NavType.IntType
                                defaultValue = 0 
                            })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("idPelicula") ?: 0
                            PantallaFormulario(
                                viewModel = viewModel,
                                peliculaId = id,
                                onVolver = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "detalle/{idPelicula}",
                            arguments = listOf(navArgument("idPelicula") { 
                                type = NavType.IntType 
                            })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("idPelicula") ?: 0
                            PantallaDetalle(
                                viewModel = viewModel,
                                peliculaId = id,
                                onVolver = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}