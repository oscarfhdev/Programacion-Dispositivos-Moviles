package com.example.tetris

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val rectanguloView: View = findViewById(R.id.rectangulo);

        val rectangulo: Rectangulo = Rectangulo(ContextCompat.getColor(this, R.color.cyan), 100, 100)

        val btnArriba: Button = findViewById(R.id.btnArriba)
        val btnAbajo: Button = findViewById(R.id.btnAbajo)
        val btnIzquierda: Button = findViewById(R.id.btnIzquierda)
        val btnDerecha: Button = findViewById(R.id.btnDerecha)
        val btnCambiarTamanio: Button = findViewById(R.id.btnCambiarTamanio)
        val btnCambiarColor: Button = findViewById(R.id.btnCambiarColor)
    }

}