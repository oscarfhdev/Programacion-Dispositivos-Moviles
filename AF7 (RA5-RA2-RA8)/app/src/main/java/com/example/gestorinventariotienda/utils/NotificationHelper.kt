package com.example.gestorinventariotienda.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.gestorinventariotienda.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Canal único para nuestras notificaciones de la tienda.
    // Esto es obligatorio desde Android 8.0 (Oreo).
    private val CHANNEL_ID = "inventory_channel_id"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Avisos de Inventario"
            val descriptionText = "Notificaciones de stock bajo y reportes PDF"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            // Registramos el canal en el sistema
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Usaremos esta función para avisar sobre el stock bajo de un producto
    fun showLowStockNotification(productName: String, currentStock: Int) {
        showNotification(
            id = productName.hashCode(), // Un ID único basado en el nombre para no sobreescribir si son distintos
            title = "¡Alerta de Stock Bajo!",
            message = "El producto $productName solo tiene $currentStock unidades disponibles."
        )
    }

    // Usaremos esta para avisar que el PDF se generó con éxito
    fun showPdfGeneratedNotification(fileName: String) {
        showNotification(
            id = 9999, // ID fijo para el reporte
            title = "Reporte Generado",
            message = "El inventario se ha guardado como $fileName."
        )
    }

    // Función base que arma y lanza la notificación nativa
    private fun showNotification(id: Int, title: String, message: String) {
        // Intent para abrir la app al tocar la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Construimos la notificación visual
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Icono del sistema por defecto
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Desaparece al tocarla

        // La lanzamos
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, builder.build())
    }
}
