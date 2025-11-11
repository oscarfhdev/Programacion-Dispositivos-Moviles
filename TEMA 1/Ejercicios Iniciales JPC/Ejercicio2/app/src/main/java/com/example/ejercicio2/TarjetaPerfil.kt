package com.example.ejercicio2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource

@Composable // Definimos la función que genera UI
fun tarjetaPerfil() {
    // Creamos una columna
    Column(
        modifier = Modifier
            // Va a ocupar toda la pantalla, añadimos espaciado interior
            .fillMaxSize()
            .padding(20.dp),
        // Va a estar centrado tanto horizontal como verticalmente
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Creamos una fila dentro de la columna
        Row(
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(16.dp)) // Sombra primero
                .background(Color.White, RoundedCornerShape(16.dp)) // Fondo con bordes
                .padding(16.dp) // Espaciado interior
                .fillMaxWidth(), // Ocupa todo lo que puede

            // Aquí lo centramos verticalmente para que se quede en el centro de la pantalla
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Añadimos la imagen
            Image(
                // No hace falta añadir la extensión(.png), solo que esté dentro de res/drawable
                painter = painterResource(id = R.drawable.mifoto),
                contentDescription = "Foto de perfil", // Le ponemos una descripción

                // Modificamos el tamaño y la redondeamos con "clip"
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp))
            )

            // Separamos la imagen de los elementos de la derecha
            Spacer(modifier = Modifier.width(16.dp))

            // Ahora creamos otra columna
            Column() {
                // Añadimos los textos, editamos el tipo de letra y el color
                Text("Óscar Fernández", style = MaterialTheme.typography.titleMedium)
                Text("Desarrollador FullStack", fontSize = 14.sp, color = Color.Gray)

                // Separamos un poco los botones del texto de arriba
                Spacer(modifier = Modifier.height(8.dp))

                // Esta fila va a contener los dos botones
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { // Con spacedBy separamos un poco los dos botones
                    // El primero botón, le ponemos un color azul
                    Button(onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.teal_200)
                        )
                        // Este es el texto del botón
                        ) { Text("Seguir") }

                    // Este es el otro botón
                    OutlinedButton(onClick = {}

                    ) { Text("Mensaje") }
                }
            }
        }
    }
}


// Con esto podemos ver poco a poco el desarrollo de la app
@Preview(showBackground = true)
@Composable
fun PreviewTarjetaPerfil() {
    MaterialTheme {
        tarjetaPerfil()
    }
}
