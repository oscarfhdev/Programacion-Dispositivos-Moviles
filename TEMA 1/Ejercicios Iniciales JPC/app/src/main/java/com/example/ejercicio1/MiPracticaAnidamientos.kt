package com.example.ejercicio1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable // Le ponemos esta anotación para indicar que genera UI
fun MiPracticaConAnidamientos() {  // Definimos la función para ser llamada desde el mainActivity

    // ESto organiza los hijos verticalmente
    Column(
        // Aqui le hacemos unos ajustes como poner el ancho completo, el color de fondo azul y espaciado
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF03A9F4))
            .padding(16.dp),

        // Los hijos están alineados verticalmente hacia arriba y centrados horizontalmente
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ahora ponemos el texto
        Text(
            text = "Práctica de Filas, Columnas y Anidamientos",
            style = MaterialTheme.typography.titleLarge, // Aumentamos el tamaño
            modifier = Modifier
                .fillMaxWidth() // Le decimos que ocupe todo el anchyo
                .padding(vertical = 20.dp) // Le ponemos espaciado
        )


        // Esto es la fila (row) principal con dos columnas dentro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCDDC39)), // Lo ponemos de color verda
            horizontalArrangement = Arrangement.SpaceBetween, // Separa a las columnas entre ellas
            verticalAlignment = Alignment.Top // Lo alinemamos verticalmente arriba
        ) {
            // Esto es la columna izquierda
            Column(
                modifier = Modifier
                    .weight(1f) // CAda una de las columnas coge 1 fracción
                    .padding(8.dp) // Ponemos espacio exterior
                    .background(Color(0xFFDDEBF7))
                    .padding(8.dp), //Ponemos espacio interior
            ) {
                // Ponemos un encabezado con el texto mediano
                Text("Columna Izquierda", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp)) // Esto separa el texto un poco
                Text("Elemento A")
                Text("Elemento B")
            }

            // Espaciamos por la mitad entre las dos columnas
            Spacer(Modifier.width(12.dp))

            // Columna derecha
            Column(
                // Modificamos la columna
                modifier = Modifier
                    .weight(1f) // Le damos la otra fracción
                    .padding(8.dp) // Espacio exterior
                    .background(Color(0xFFFDE9D9)) // Color
                    .padding(8.dp), // Espacio interior

                // Lo centramos arriba verticalmetne
                verticalArrangement = Arrangement.Top,
            ) {
                // Ponemos el encabezado con el texto medio
                Text("Columna Derecha", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp)) // Y un espacio

                // Ahora tenemos dentro de esta columna una fila con tres elementos
                Row(
                    // Va a ocupar el ancho entero del padre
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Con esto ponemos espacio entre ellos
                ) {
                    Text("1")
                    Text("2")
                    Text("3")
                }
            }
        }

        // Ponemos otro espacio para separar bien las dos filas
        Spacer(modifier = Modifier.height(12.dp))

        // Parte 3: Ahora aquí, hacemos lo mismo que en la anterior
        Row(
            // Modificamos las principales propiedades
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEFEF))
                .padding(8.dp),
            // Configuramos verticalmente arriba, y el contenido separado equitativamente (spaceEnvenly)
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            Text("Opción 1")
            Text("Opción 2")
            Text("Opción 3")
        }
    }
}

// Con esto creamos una vista previa sencilla, ejecutando la función
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
// Ejecutamos la función de arriba, gracias a preview vemos la vista previa
fun PreviewMiPracticaConAnidamientos() {
    MaterialTheme {
        Surface {
            MiPracticaConAnidamientos()
        }
    }
}