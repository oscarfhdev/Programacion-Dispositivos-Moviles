package com.example.multimediacomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.multimediacomposeapp.ui.theme.MultimediaComposeAppTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultimediaComposeAppTheme { // Reemplaza esto con el nombre de tu tema
                val navController = rememberNavController()
                NavHost(
                navController = navController,
                startDestination = NavRoutes.Menu.route
                ) {
                    // 1. Pantalla del menú (la primera que se muestra)
                    composable(NavRoutes.Menu.route) { MenuScreen(navController) }

                    // 2. Pantalla de Audio
                    composable(NavRoutes.Audio.route) { AudioScreen() }

                    // 3. Pantalla de Video
                    composable(NavRoutes.Video.route) { VideoScreen() }
                    // 4. Pantalla de Cámara
                    composable(NavRoutes.Camera.route) { CameraScreen() }

                    // 5. Pantalla de Grabadora
                    composable(NavRoutes.Recorder.route) { RecorderScreen() }
                }

            }
        }
    }
}