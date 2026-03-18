# 📦 StockMatic

> Aplicación móvil Android para la gestión de inventario en tiempo real, desarrollada con **Kotlin**, **Jetpack Compose** y **Clean Architecture**.

---

## 📋 Descripción General

**StockMatic** es una aplicación Android diseñada para que el personal de una tienda minorista pueda controlar su catálogo de productos directamente desde el móvil. Permite sincronizar productos desde una API REST, gestionar el stock de forma local, generar informes en PDF, visualizar estadísticas y reportar incidencias, todo con soporte offline gracias a base de datos local.

---

## 🚀 Características Principales

| Funcionalidad | Descripción |
|---|---|
| 🌐 **Sincronización Wi-Fi** | Descarga productos desde la [FakeStore API](https://fakestoreapi.com) solo cuando el dispositivo está conectado a la red Wi-Fi autorizada de la tienda.|
| 📋 **Gestión de Stock** | Lista completa de productos con control de unidades (`+` / `−`) con alertas visuales de stock bajo. |
| 🔔 **Notificaciones Locales** | Aviso automático cuando el stock de un producto baja de 5 unidades o cuando se exporta un PDF. |
| 📄 **Exportación a PDF** | Genera un informe PDF del inventario completo y lo guarda en el almacenamiento del dispositivo. |
| 📊 **Panel de Estadísticas** | Gráfico Donut animado que muestra la proporción de productos con stock normal vs. stock bajo. |
| ⚙️ **Preferencias** | Configuración del nombre de la tienda y SSID Wi-Fi autorizado, persistidos con DataStore. |
| 🐛 **Buzón de Incidencias** | Formulario para reportar errores o problemas, con historial completo guardado en base de datos local. |

---

## 🏗️ Arquitectura

La aplicación implementa **Clean Architecture** dividida en tres capas bien diferenciadas, siguiendo el patrón **MVVM**:

```
app/
└── src/main/java/com/example/gestorinventariotienda/
    ├── data/                  # Capa de datos
    │   ├── local/             # Room (entidades y DAOs)
    │   ├── network/           # Retrofit (API y modelos)
    │   ├── preferences/       # DataStore (SettingsManager)
    │   └── repository/        # Implementación del repositorio
    ├── domain/                # Capa de dominio
    │   └── repository/        # Contrato del repositorio (interfaz)
    ├── ui/                    # Capa de presentación
    │   ├── screens/           # Pantallas Compose
    │   ├── viewmodels/        # ViewModels (MVVM)
    │   ├── navigation/        # Navegación (NavHost + BottomBar)
    │   └── theme/             # Tema Material 3
    ├── utils/                 # Utilidades transversales
    │   ├── NetworkMonitor     # Control de conectividad Wi-Fi
    │   ├── PdfGenerator       # Generación de informes PDF
    │   └── NotificationHelper # Notificaciones locales
    └── di/                    # Módulos de inyección (Hilt)
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Uso |
|---|---|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** | UI declarativa |
| **Material Design 3** | Sistema de diseño |
| **Dagger Hilt** | Inyección de dependencias |
| **Room** | Base de datos local SQLite |
| **DataStore Preferences** | Persistencia de preferencias de usuario |
| **Retrofit + OkHttp** | Cliente HTTP para la API REST |
| **Navigation Compose** | Navegación entre pantallas |
| **Coil** | Carga asíncrona de imágenes |
| **Coroutines + Flow** | Programación asíncrona reactiva |
| **Canvas API** | Gráfico Donut dibujado de forma nativa |

---

## 📱 Pantallas de la Aplicación

### 🗂️ Pantalla de Inventario (`InventoryScreen`)
- Lista de todos los productos con imagen, nombre, categoría, precio y stock actual.
- Botón **⬇️** en la cabecera para sincronizar el catálogo con la API.
- Tarjetas en **rojo** para productos con stock < 5 unidades.
- Botón flotante **📄** para exportar el inventario completo a PDF.

### 📊 Pantalla de Panel (`DashboardScreen`)
- Gráfico **Donut animado** con Canvas nativo de Compose.
- Leyenda con número de productos en stock normal (🟢) y stock bajo (🔴).

### ⚙️ Pantalla de Ajustes y Buzón (`SettingsScreen`)
- Campos de configuración de nombre de tienda y red Wi-Fi.
- Botón para **redactar un reporte de incidencia** con un diálogo emergente.
- Historial completo de todas las incidencias registradas.

---

## ⚙️ Configuración y Primeros Pasos

### Requisitos
- Android Studio Hedgehog (o superior)
- Android SDK 24 (API Level 24+)
- Conexión a internet para la primera sincronización

### Instalación
```bash
# Clonar el repositorio
git clone https://github.com/oscarfhdev/Programacion-Dispositivos-Moviles.git

# Abrir la carpeta del proyecto en Android Studio:
# AF7 (RA5-RA2-RA8)/
```

### Compilación y Ejecución
```bash
# Compilar el proyecto
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug
```

---

## 📖 Manual de Usuario

### Paso 1 — Configurar la aplicación
1. Abre la pestaña **Ajustes y Buzón** (⚙️).
2. Introduce el **nombre de tu tienda**.
3. Introduce el **SSID** de tu red Wi-Fi (el nombre exacto como aparece en el móvil).
4. Los cambios se guardan automáticamente.

### Paso 2 — Descargar el catálogo de productos
1. Ve a la pestaña **Inventario** (📋).
2. Conéctate a la red Wi-Fi que configuraste en el paso anterior.
3. Pulsa el botón **⬇️** en la esquina superior derecha.
4. La app verificará la red e importará el catálogo desde internet.

### Paso 3 — Gestionar el stock
- Pulsa **`+`** para añadir unidades a un producto.
- Pulsa **`−`** para restar unidades.
- Si el stock de un producto baja de **5 unidades**, la tarjeta se marca en rojo y recibirás una **notificación**.

### Paso 4 — Exportar a PDF
1. En la pantalla **Inventario**, pulsa el botón flotante **📄**.
2. Se generará un archivo PDF con el inventario completo.
3. Recibirás una notificación cuando el archivo esté listo.

### Paso 5 — Consultar estadísticas
- Abre la pestaña **Panel** (📊).
- El gráfico Donut muestra visualmente la distribución de stock.

### Paso 6 — Reportar una incidencia
1. Ve a **Ajustes y Buzón** (⚙️).
2. Pulsa **"Redactar Reporte de Incidencia"**.
3. Describe el problema y pulsa **Guardar**.
4. La incidencia queda registrada en el historial con fecha y hora.

---

## 🗄️ Modelo de Datos

### `ProductEntity` (tabla `products`)
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Int (PK) | ID del producto (viene de la API) |
| `title` | String | Nombre del producto |
| `price` | Double | Precio en dólares |
| `quantity` | Int | Stock actual (por defecto: 0) |
| `category` | String | Categoría del producto |
| `imageUrl` | String | URL de la imagen del producto |

### `IncidentReportEntity` (tabla `incident_reports`)
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Int (PK, autoincrement) | ID de la incidencia |
| `description` | String | Descripción del problema |
| `timestamp` | Long | Fecha y hora del reporte (Unix time) |

---

## 🔒 Seguridad y Privacidad

- **Validación de red Wi-Fi**: la sincronización con la API solo se ejecuta si el dispositivo está conectado a la red Wi-Fi configurada, evitando descargas no autorizadas.
- **Compilación Release con R8**: en la versión de producción se aplica ofuscación y reducción de código con R8.
- **Sin datos sensibles en red**: la app no transmite datos personales al servidor, solo realiza lecturas del catálogo público.

---

## 🧪 Tecnologías de Prueba

| Framework | Propósito |
|---|---|
| **JUnit 4** | Pruebas unitarias de ViewModels y repositorios |
| **Compose UI Test** | Pruebas de interfaz de usuario |
| **Hilt Android Testing** | Inyección de dependencias en entorno de pruebas |
| **kotlinx-coroutines-test** | Pruebas de código asíncrono con Flow |

---

## 📁 Estructura de Módulos Gradle

```
# Plugins principales
- com.android.application
- org.jetbrains.kotlin.android
- org.jetbrains.kotlin.plugin.compose
- com.google.devtools.ksp
- com.google.dagger.hilt.android
```

---

## 👨‍💻 Autor

**Óscar Fernández Herrera**  
2º DAM — IES Antonio Gala (Palma del Río)  
Módulo: Programación Multimedia y Dispositivos Móviles + Desarrollo de Interfaces

---

## 📜 Licencia

Este proyecto ha sido desarrollado con fines educativos para la Actividad Final 7 (AF7) del CFGS de Desarrollo de Aplicaciones Multiplataforma.
