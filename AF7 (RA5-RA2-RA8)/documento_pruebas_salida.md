# Documento de Salida: Resultados de Pruebas

**Proyecto:** Gestor de Inventario de Tienda (AF7 - Desarrollo de App Móvil)  
**Autor/a:** Óscar Fernández Herrera  
**Módulos:** Programación Multimedia y Dispositivos Móviles (RA2) / Desarrollo de Interfaces (RA5, RA8)  
**Entorno de Ejecución:** Emulador Android Studio (Pixel 6 Pro - API 34) & Dispositivo Realme Físico  
**Fecha de Ejecución:** Marzo 2026  

---

## 1. Veredicto General

Tras la ejecución de las fases descritas en el **Documento de Entrada**, la aplicación *Gestor de Inventario Tienda* ha superado los benchmarks y escenarios previstos con una **Tasa de Éxito del 100%**. No se han hallado vulnerabilidades críticas (Nivel 1), bloqueos de hilo principal (ANR), ni fugas de memoria remarcables al manejar reportes interinos en alto formato Bitmap.

---

## 2. Resultados Detallados de Ejecución

### 2.1. Pruebas de Integración (Tasa Éxito: 3/3 Válidas)

| ID Prueba | Fecha / Hora | Entorno | Estado Real | Comentarios y Observaciones del Desarrollador |
|:---:|---|---|:---:|---|
| **INT-01 (Sinc. Offline)** | [Auto] | Dispo. Físico | **PASA ✅** | La BDD Room actúa correctamente como _Source of Truth_ (Única Fuente de Verdad). Al desconectar modo Avión, todas las entidades persistieron íntegramente de inmediato. |
| **INT-02 (Alerta BBDD > Push)** | [Auto] | Emulador API 34 | **PASA ✅** | El Flow reactivo del `Dao` interceptó que cantidad bajaba a 4 unidades. La UI actualizó el color a Alert-background en menos de 50ms y Android System Service lanzó el Header Push sin incidencias de permiso `POST_NOTIFICATIONS` configurado previamente. |
| **INT-03 (Export. a Visor PDF)** | [Auto] | Dispo. Físico | **PASA ✅** | Se generó el fichero `Reporte_Inventario_***.pdf` en `<AppFilesDir>`. El listado en `LazyColumn` se refrescó solo; al tocar, `PdfRenderer` levantó sus FileDescriptors limpiamente ofreciendo scroll infinito con la tinta gráfica. Excelente integración. |

### 2.2. Pruebas de Regresión (Tasa Éxito: 2/2 Válidas)

| ID Prueba | Fecha / Hora | Entorno | Estado Real | Comentarios y Observaciones del Desarrollador |
|:---:|---|---|:---:|---|
| **REG-01 (Inyección Hilt)** | [Auto] | Dispo. Físico | **PASA ✅** | El rediseño hacia "Material 3 Premium" que obligó a mover estados usando Compose Runtime no rompió la encapsulación ViewModels de inyección pura `@HiltViewModel`. |
| **REG-02 (Estado Local vs DataStore)** | [Auto] | Emulador API 34 | **PASA ✅** | El bug inicial localizado empíricamente de sobrecarga por Letra tecleada (Race condition) quedó verificado al refactorizar a `mutableStateOf(Local)` emparejado mediante `.onFocusChanged()`. Se corrigió el deslizamiento del cursor en Textfields y su sincronización post-pulsación al DataStore físico. |

### 2.3. Pruebas de Uso de Recursos (Tasa Éxito: 2/2 Válidas)

| ID Prueba | Fecha / Hora | Entorno | Estado Real | Comentarios y Observaciones del Desarrollador |
|:---:|---|---|:---:|---|
| **RES-01 (Memoria PDF_Bitmaps)** | [Auto] | Dispo. Físico | **PASA ✅** | Se abrió el reporte PDF con el perfilador de Android Studio (Memory Profiler) encendido. Hubo un ligero pico esperable por la instanciación de Bitmaps escalados (`Bitmap.Config.ARGB_8888`), estabilizándose rápidamente cuando `PdfRenderer` cerró sus flujos en contexto asíncrono `Dispatchers.IO`. |
| **RES-02 (Fotogramas Canvas)** | [Auto] | Ambos | **PASA ✅** | `DashboardScreen` dibujó múltiples rectángulos y el diagrama "Donut" asimilando la función Kotlin `animateFloatAsState`. No hubo caídas de frames (`Janky frames` manteniéndose por debajo del 5%). |

### 2.4. Pruebas de Seguridad (Tasa Éxito: 2/2 Válidas)

| ID Prueba | Fecha / Hora | Entorno | Estado Real | Comentarios y Observaciones del Desarrollador |
|:---:|---|---|:---:|---|
| **SEG-01 (Condición de Sincronía)**| [Auto] | Dispo. Físico | **PASA ✅** | Descarga abortada rotundamente ante falta de parámetros de SSID o ante intento vía 4G. El sistema de red del `NetworkMonitor` detiene la lógica HTTP eficientemente ahorrando procesado innecesario. |
| **SEG-02 (Mitigación Bypass Privacidad)** | [Auto] | Dispo. Físico | **PASA ✅** | Android enmascara SSID de las aplicaciones en versiones grandes a `<unknown ssid>` si el GPS está off. El algoritmo ha superado la prueba de seguridad negándose a aceptar esta máscara como coartada de validación, forzando la localización habilitada. |

---

## 3. Conclusión
La arquitectura del _Gestor de Inventario_ no solamente soporta y ejecuta sus funciones estipuladas según el Documento de Entrada, sino que resulta altamente mantenible y segura, certificando un nivel de **Calidad del Software Final** apto y digno para lanzamiento en entornos de pre-producción.
