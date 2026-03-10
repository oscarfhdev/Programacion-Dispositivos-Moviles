package com.example.gestorinventariotienda.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gestorinventariotienda.ui.screens.DashboardScreen
import com.example.gestorinventariotienda.ui.screens.InventoryScreen
import com.example.gestorinventariotienda.ui.screens.SettingsScreen

// Rutas selladas para evitar errores tipográficos al navegar
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : Screen("inventory", "Inventario", Icons.Default.List)
    object Dashboard : Screen("dashboard", "Panel", Icons.Default.BarChart)
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
            modifier = Modifier.padding(innerPadding)
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
        }
    }
}

// Botonera de navegación inferior
@Composable
fun BottomNavigationBar(navController: NavController, items: List<Screen>) {
    NavigationBar {
        // Leemos en qué pantalla estamos actualmente
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    // Evitamos que se abra la misma pantalla múltiples veces
                    if (currentRoute != screen.route) {
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
