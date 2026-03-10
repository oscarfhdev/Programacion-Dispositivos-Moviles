package com.example.gestorinventariotienda

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Marcamos la clase Application con @HiltAndroidApp para generar
// todos los componentes de la inyección de dependencias en el arranque.
@HiltAndroidApp
class GestorInventarioApp : Application()
