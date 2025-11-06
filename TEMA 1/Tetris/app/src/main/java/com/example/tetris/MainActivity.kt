package com.example.tetris

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rectanguloView: View = findViewById(R.id.rectangulo);
        rectanguloView.post {
            val btnArriba: Button = findViewById(R.id.btnArriba)
            val btnAbajo: Button = findViewById(R.id.btnAbajo)
            val btnIzquierda: Button = findViewById(R.id.btnIzquierda)
            val btnDerecha: Button = findViewById(R.id.btnDerecha)
            val btnCambiarTamanio: Button = findViewById(R.id.btnCambiarTamanio)
            val btnCambiarColor: Button = findViewById(R.id.btnCambiarColor)
            val btnCambiarColorBorde: Button = findViewById(R.id.btnCambiarColorBorde)


            val inicialX = rectanguloView.x.toInt()
            val inicialY = rectanguloView.y.toInt()

            val inicialWidth = rectanguloView.width
            val inicialHeigth = rectanguloView.height

            val rectangulo: RectanguloConBordes = RectanguloConBordes(
                ContextCompat.getColor(this, R.color.cyan),
                inicialWidth,
                inicialHeigth
            ).apply { x = inicialX; y = inicialY; bordeColor = ContextCompat.getColor(this@MainActivity, R.color.black) }


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
                actualizarVista(rectangulo, rectanguloView)

            }

            btnDerecha.setOnClickListener {
                rectangulo.movDcha()
                actualizarVista(rectangulo, rectanguloView)

            }

            btnCambiarTamanio.setOnClickListener {
                rectangulo.cambiarTamano(200, 200)
                actualizarVista(rectangulo, rectanguloView)

            }

            btnCambiarColor.setOnClickListener {
                rectangulo.color = generarColorAleatorio()
                actualizarVista(rectangulo, rectanguloView)
            }

            btnCambiarColorBorde.setOnClickListener {
                rectangulo.cambiarColorBorde(generarColorAleatorio())
                actualizarVista(rectangulo, rectanguloView)
            }

            btnCambiarColorBorde.setOnClickListener {
                actualizarVista(rectangulo, rectanguloView)
            }
        }
    }

    private fun actualizarVista(rectangulo: Rectangulo, rectanguloView: View){

        // Al rectanguloView la da el valor del rectangulo clase Rectangulo
        rectanguloView.layoutParams.width = rectangulo.ancho
        rectanguloView.layoutParams.height = rectangulo.alto

        val drawable = GradientDrawable()
        drawable.setColor(rectangulo.color)
        drawable.setStroke(10, rectangulo.color)
//        rectanguloView.setBackgroundColor(rectangulo.color)

        rectanguloView.x = rectangulo.x.toFloat()
        rectanguloView.y = rectangulo.y.toFloat()

        // método para que tome efecto, solicita de nuevo el layout
        rectanguloView.requestLayout()
    }

    fun generarColorAleatorio() : Int {
        val color = Random.nextInt();

        return color
    }

}