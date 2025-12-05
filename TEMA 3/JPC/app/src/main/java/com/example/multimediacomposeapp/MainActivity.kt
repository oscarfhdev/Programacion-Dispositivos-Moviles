package com.example.multimediacomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.multimediacomposeapp.ui.theme.MultimediaComposeAppTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

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

@Composable
fun MenuScreen (navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Menú principal", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = { navController.navigate (NavRoutes.Audio.route) }) {
            Text("Reproductor de Audio")
        }

        Button(onClick = { navController.navigate (NavRoutes.Video.route) }) {
            Text("Reproductor de Video")
        }

        Button(onClick = { navController.navigate (NavRoutes.Camera.route) }) {
            Text("Cámara (Intent)")
        }

        Button(onClick = { navController.navigate (NavRoutes.Recorder.route) }) {
            Text("Grabadora (Intent)")
        }
    }
}

@Composable
fun AudioScreen() {
    val context = LocalContext.current

    // MediaPlayer dentro de remember para que no se recree cada vez que Compose recomponga la pantalla [cite: 138, 166]
    val mediaPlayer = remember {
        // Crea el MediaPlayer y carga el recurso de audio [cite: 141, 164]
        MediaPlayer.create(context, R.raw.audio)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Reproductor de Audio", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        // Botón PLAY (inicia la reproducción)
        Button(onClick = { mediaPlayer.start() }) {
            Text("Play")
        }

        // Botón PAUSE
        Button(onClick = { mediaPlayer.pause() }) {
            Text("Pause")
        }

        // Botón STOP (detiene y reinicia al principio, aunque en MediaPlayer se usa más comúnmente release/seek)
        Button(onClick = {
            mediaPlayer.stop()
            // Se debe preparar de nuevo después de stop() para volver a usarlo
            mediaPlayer.prepare()
        }) {
            Text("Stop")
        }
    }
}

@Composable
fun VideoScreen() { /* Contenido en PASO 8 */ }

@Composable
fun CameraScreen() { /* Contenido en PASO 10 */ }

@Composable
fun RecorderScreen() { /* Contenido en PASO 11 */ }