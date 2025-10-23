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

        val rectangulo: Rectangulo = Rectangulo(ContextCompat.getColor(this, R.color.cyan), 200, 200)

        val btnArriba: Button = findViewById(R.id.btnArriba)
        val btnAbajo: Button = findViewById(R.id.btnAbajo)
        val btnIzquierda: Button = findViewById(R.id.btnIzquierda)
        val btnDerecha: Button = findViewById(R.id.btnDerecha)
        val btnCambiarTamanio: Button = findViewById(R.id.btnCambiarTamanio)
        val btnCambiarColor: Button = findViewById(R.id.btnCambiarColor)

        btnArriba.setOnClickListener {
            rectangulo.movArriba()
            actualizarVista(rectangulo, rectanguloView)
        }

        btnAbajo.setOnClickListener {
            rectangulo.movAbajo()
            actualizarVista(rectangulo, rectanguloView)
        }

        btnIzquierda.setOnClickListener {
            rectangulo.movIzqda()
            actualizarVista(rectangulo,rectanguloView)

        }

        btnDerecha.setOnClickListener {
            rectangulo.movDcha()
            actualizarVista(rectangulo,rectanguloView)

        }

        btnCambiarTamanio.setOnClickListener {
            rectangulo.cambiarTamano(200, 200)
            actualizarVista(rectangulo,rectanguloView)

        }

        btnCambiarColor.setOnClickListener {
            rectangulo.color = ContextCompat.getColor(this, R.color.black)
            actualizarVista(rectangulo,rectanguloView)
        }

    }

    private fun actualizarVista(rectangulo: Rectangulo, rectanguloView: View){

        // Al rectanguloView la da el valor del rectangulo clase Rectangulo
        rectanguloView.layoutParams.width = rectangulo.ancho
        rectanguloView.layoutParams.height = rectangulo.alto

        rectanguloView.setBackgroundColor(rectangulo.color)

        rectanguloView.x = rectangulo.x.toFloat()
        rectanguloView.y = rectangulo.y.toFloat()

        // método para que tome efecto, solicita de nuevo el layout
        rectanguloView.requestLayout()

    }

}