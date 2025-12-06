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
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.VideoView
import android.net.Uri
import androidx.compose.foundation.layout.Box
import android.provider.MediaStore
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultimediaComposeAppTheme {
                // Creamos el controlador de navegación
                val navController = rememberNavController()

                // Definimos el contenedor de navegación
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Menu.route
                ) {
                    // 1. Pantalla del Menú
                    composable(NavRoutes.Menu.route) { MenuScreen(navController) }

                    // 2. Pantalla de Audio (le pasamos el controlador para poder volver)
                    composable(NavRoutes.Audio.route) { AudioScreen(navController) }

                    // 3. Pantalla de Vídeo (le pasamos el controlador)
                    composable(NavRoutes.Video.route) { VideoScreen(navController) }

                    // 4. Pantalla de Cámara (le pasamos el controlador)
                    composable(NavRoutes.Camera.route) { CameraScreen(navController) }

                    // 5. Pantalla de Grabadora (le pasamos el controlador)
                    composable(NavRoutes.Recorder.route) { RecorderScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icono decorativo del menú
        Icon(Icons.Filled.Home, contentDescription = "Casa", modifier = Modifier.padding(bottom = 16.dp))
        Text("Menú Multimedia", style = MaterialTheme.typography.headlineMedium)

        // Botón para ir al Audio
        Button(onClick = { navController.navigate(NavRoutes.Audio.route) }) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Audio + Volumen")
        }

        // Botón para ir al Vídeo
        Button(onClick = { navController.navigate(NavRoutes.Video.route) }) {
            Icon(Icons.Filled.Star, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Vídeo + Controles")
        }

        // Botón para ir a la Cámara
        Button(onClick = { navController.navigate(NavRoutes.Camera.route) }) {
            Icon(Icons.Filled.Face, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Cámara")
        }

        // Botón para ir a la Grabadora
        Button(onClick = { navController.navigate(NavRoutes.Recorder.route) }) {
            Icon(Icons.Filled.Call, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Grabadora")
        }
    }
}

@Composable
fun AudioScreen(navController: NavController) {
    val context = LocalContext.current

    // Aquí guardamos el estado del volumen
    var volume by remember { mutableFloatStateOf(0.5f) }

    // Creamos el reproductor y cargamos el archivo de audio
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.audio).apply {
            setVolume(0.5f, 0.5f)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Reproductor de Audio", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Botones para controlar la reproducción
            Button(onClick = { mediaPlayer.start() }) { Text("Play") }
            Button(onClick = { mediaPlayer.pause() }) { Text("Pause") }
            Button(onClick = {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
            }) { Text("Stop") }
        }

        Spacer(Modifier.height(30.dp))
        Text(text = "Volumen: ${(volume * 100).toInt()}%")

        // Slider para cambiar el volumen
        Slider(
            value = volume,
            onValueChange = { newVolume ->
                volume = newVolume
                mediaPlayer.setVolume(volume, volume)
            },
            valueRange = 0f..1f
        )

        Spacer(Modifier.height(40.dp))

        // Botón para volver atrás
        Button(onClick = {
            // Paramos el audio si está sonando antes de salir
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            // Nos devuelve a la pantalla anterior (Menú)
            navController.popBackStack()
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Volver al Menú")
        }
    }
}

@Composable
fun VideoScreen(navController: NavController) {
    val context = LocalContext.current

    // Creamos el VideoView y cargamos el vídeo
    val videoView = remember {
        VideoView(context).apply {
            val uri = "android.resource://${context.packageName}/${R.raw.video}"
            setVideoURI(Uri.parse(uri))
            start()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor para mostrar el vídeo
        Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            AndroidView(
                factory = { videoView },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Controles de Vídeo", style = MaterialTheme.typography.headlineSmall)

        // Botones de control del vídeo
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Button(onClick = { videoView.start() }) { Text("Play") }
            Button(onClick = { videoView.pause() }) { Text("Pausa") }
            Button(onClick = {
                videoView.pause()
                videoView.seekTo(0)
            }) { Text("Stop") }
        }

        Spacer(Modifier.height(40.dp))

        // Botón para volver atrás
        Button(onClick = {
            // Nos devuelve a la pantalla anterior
            navController.popBackStack()
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Volver al Menú")
        }
    }
}

@Composable
fun CameraScreen(navController: NavController) {
    // Variable para guardar la foto
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Preparamos el lanzador para recibir la foto
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Aquí comprobamos si la foto se hizo bien
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap = bitmap
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Cámara", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        // Botón para abrir la cámara
        Button(onClick = {
            // Creamos el intent para pedir una captura de imagen
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.launch(intent)
        }) {
            Text("Abrir cámara")
        }

        // Si tenemos foto, la mostramos aquí
        imageBitmap?.let {
            Spacer(Modifier.height(20.dp))
            Text("Foto capturada:")
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Foto",
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(40.dp))

        // Botón para volver atrás
        Button(onClick = {
            // Nos devuelve al menú
            navController.popBackStack()
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Volver al Menú")
        }
    }
}

@Composable
fun RecorderScreen(navController: NavController) {
    // Preparamos el lanzador para la grabadora
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Grabadora de sonido", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        // Botón para grabar audio
        Button(onClick = {
            // Creamos el intent de grabación
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            // Usamos try-catch para proteger la app
            try {
                launcher.launch(intent)
            } catch (e: Exception) {
                println("Error: No se encontró app de grabación")
            }
        }) {
            Text("Grabar audio")
        }

        Spacer(Modifier.height(40.dp))

        // Botón para volver atrás
        Button(onClick = {
            // Nos devuelve al menú
            navController.popBackStack()
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Volver al Menú")
        }
    }
}