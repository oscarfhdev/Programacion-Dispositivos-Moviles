package com.example.gestorinventariotienda.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.gestorinventariotienda.data.local.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Clase utilitaria encargada de generar el PDF. Le inyectamos el contexto 
// porque lo necesita para acceder al directorio de archivos del dispositivo.
@Singleton
class PdfGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Función suspendida porque puede tardar dependiendo de la lista de productos
    suspend fun generateInventoryPdf(products: List<ProductEntity>, storeName: String): Result<File> {
        // Nos movemos al hilo de IO (Entrada/Salida) porque estamos escribiendo en el disco
        return withContext(Dispatchers.IO) {
            try {
                // 1. Iniciamos el documento PDF
                val pdfDocument = PdfDocument()

                // 2. Configuramos la página (A4 estándar: 595x842 puntos)
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)

                // 3. Obtenemos el "Canvas" que es nuestro lienzo para dibujar texto y formas
                val canvas: Canvas = page.canvas

                // 4. Preparamos el "pincel" (Paint) para escribir el texto
                val titlePaint = Paint().apply {
                    color = Color.BLACK
                    textSize = 24f
                    isFakeBoldText = true
                    textAlign = Paint.Align.CENTER
                }

                val textPaint = Paint().apply {
                    color = Color.DKGRAY
                    textSize = 14f
                }

                // 5. Dibujamos el Título del reporte
                canvas.drawText("Reporte de Inventario - $storeName", pageInfo.pageWidth / 2f, 50f, titlePaint)

                // 6. Dibujamos la fecha
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val currentDate = dateFormat.format(Date())
                canvas.drawText("Fecha: $currentDate", 50f, 90f, textPaint)

                // 7. Iteramos sobre los productos y los pintamos en el lienzo línea por línea
                var yPosition = 140f
                canvas.drawText("ID | PRODUCTO | CANTIDAD | PRECIO", 50f, yPosition, titlePaint.apply { textSize = 16f; textAlign = Paint.Align.LEFT })
                yPosition += 30f

                products.forEach { product ->
                    val line = "${product.id} | ${product.title.take(30)}... | Stock: ${product.quantity} | \$${product.price}"
                    canvas.drawText(line, 50f, yPosition, textPaint)
                    yPosition += 25f

                    // Si se nos acaba el espacio, en una app real habría que añadir otra página.
                    // Por simplicidad, aquí limitaremos la muestra si sobrepasamos el límite (aprox. 800)
                    if (yPosition > 800f) {
                        return@forEach
                    }
                }

                // 8. Cerramos la página
                pdfDocument.finishPage(page)

                // 9. Guardamos el archivo PDF en el almacenamiento interno de la app
                val file = File(context.filesDir, "Reporte_Inventario_${System.currentTimeMillis()}.pdf")
                pdfDocument.writeTo(FileOutputStream(file))
                
                // 10. Cerramos el documento
                pdfDocument.close()

                // Retornamos un éxito con el archivo creado
                Result.success(file)

            } catch (e: Exception) {
                // Si algo falla al escribir en memoria, capturamos el error
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }
}
