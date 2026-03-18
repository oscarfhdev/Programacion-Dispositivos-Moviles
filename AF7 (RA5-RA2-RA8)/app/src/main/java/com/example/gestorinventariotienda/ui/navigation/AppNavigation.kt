package com.example.gestorinventariotienda.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gestorinventariotienda.ui.screens.DashboardScreen
import com.example.gestorinventariotienda.ui.screens.InventoryScreen
import com.example.gestorinventariotienda.ui.screens.PdfListScreen
import com.example.gestorinventariotienda.ui.screens.PdfViewerScreen
import com.example.gestorinventariotienda.ui.screens.SettingsScreen
import androidx.compose.material.icons.filled.PictureAsPdf

// Rutas selladas para evitar errores tipográficos al navegar
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : Screen("inventory", "Inventario", Icons.Default.List)
    object Dashboard : Screen("dashboard", "Panel", Icons.Default.BarChart)
    object PdfList : Screen("pdf_list", "Reportes", Icons.Default.PictureAsPdf)
    object Settings : Screen("settings", "Ajustes", Icons.Default.Settings)
}

@Composable
fun AppNavigation() {
    // Controlador principal de Compose Navigation
    val navController = rememberNavController()
    
    // Lista de pantallas para el BottomBar
    val items = listOf(
        Screen.Inventory,
        Screen.Dashboard,
        Screen.PdfList,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = items)
        }
    ) { innerPadding ->
        // Definición de las transiciones y rutas
        NavHost(
            navController = navController,
            startDestination = Screen.Inventory.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400)) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400)) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(400)) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }
        ) {
            // Cuando la ruta sea 'inventory', pinta InventoryScreen
            composable(Screen.Inventory.route) {
                InventoryScreen()
            }
            
            // Cuando la ruta sea 'dashboard', pinta DashboardScreen
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            
            // Cuando la ruta sea 'settings', pinta SettingsScreen
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            
            // Ruta para la lista de PDFs
            composable(Screen.PdfList.route) {
                PdfListScreen(navController = navController)
            }
            
            // Ruta para el visor de un PDF concreto (pasando el nombre por URL)
            composable("pdf_viewer/{fileName}") { backStackEntry ->
                val fileName = backStackEntry.arguments?.getString("fileName") ?: ""
                PdfViewerScreen(fileName = fileName, onBack = { navController.popBackStack() })
            }
        }
    }
}

// Botonera de navegación inferior
@Composable
fun BottomNavigationBar(navController: NavController, items: List<Screen>) {
    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        // Leemos en qué pantalla estamos actualmente
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = {
                    // Evitamos que se abra la misma pantalla múltiples veces
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            // Este bloque ajusta la pila de navegación (BackStack)
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
