package com.example.casasapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores claro con la paleta inmobiliaria
private val LightColorScheme = lightColorScheme(
    primary = AzulMedio,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = Color.White,
    secondary = DoradoSuave,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = TextoOscuro,
    tertiary = VerdeSuave,
    onTertiary = Color.White,
    background = BlancoHueso,
    onBackground = TextoOscuro,
    surface = Color.White,
    onSurface = TextoOscuro,
    surfaceVariant = GrisClaro,
    onSurfaceVariant = GrisMedio,
    error = RojoSuave,
    onError = Color.White
)

// Esquema de colores oscuro
private val DarkColorScheme = darkColorScheme(
    primary = AzulClaroDark,
    onPrimary = Color.White,
    primaryContainer = AzulMedio,
    onPrimaryContainer = TextoClaro,
    secondary = DoradoDark,
    onSecondary = AzulOscuroDark,
    secondaryContainer = Color(0xFF5D4037),
    onSecondaryContainer = DoradoDark,
    tertiary = VerdeSuave,
    onTertiary = Color.White,
    background = AzulOscuroDark,
    onBackground = TextoClaro,
    surface = SuperficieDark,
    onSurface = TextoClaro,
    surfaceVariant = AzulMedio,
    onSurfaceVariant = GrisMedio,
    error = RojoSuave,
    onError = Color.White
)

@Composable
fun CasasAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Usamos siempre nuestra paleta personalizada, sin dynamic color
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Colorear la barra de estado del sistema con el color primary
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}