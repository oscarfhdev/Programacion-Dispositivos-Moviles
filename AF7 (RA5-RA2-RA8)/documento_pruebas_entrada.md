# Documento de Entrada: Diseño de Pruebas

**Proyecto:** StockMatic (AF7 - Desarrollo de App Móvil)  
**Autor/a:** Óscar Fernández Hernández  
**Módulos:** Programación Multimedia y Dispositivos Móviles (RA2) / Desarrollo de Interfaces (RA5, RA8)  
**Fecha:** Marzo 2026  

---

## 1. Introducción
El presente documento tiene por objetivo definir los escenarios, casos de uso y protocolos de evaluación que garanticen la estabilidad, seguridad y usabilidad de "StockMatic". Las pruebas están diseñadas para ejecutarse tanto en emuladores como en dispositivos físicos, cubriendo los requerimientos exigidos en la rúbrica AF7.

---

## 2. Tipos de Pruebas a Realizar

### 2.1. Pruebas de Integración
**Objetivo:** Verificar que distintos módulos independientes (Room SQLite, Retrofit API y la arquitectura Clean Architecture) funcionan correctamente en conjunto cuando intercambian información.

| ID Prueba | Descripción del Escenario | Pasos de Ejecución | Resultado Esperado |
|:---:|---|---|---|
| **INT-01** | **Sincronización Local/Remota:** Descargar productos desde FakeStoreAPI y verificar su persistencia inmediata en Room. | 1. Configurar SSID Wi-Fi válido.<br>2. Pulsar botón de Sincronizar (Inventario).<br>3. Cerrar la app y volverla a abrir (Modo Offline). | Al volver a abrir la app sin conexión, la lista de productos debe mostrarse intacta, cargada desde Room local, no desde Retrofit. |
| **INT-02** | **Alerta Temprana en UI:** Integración de Room Data con las notificaciones Locales del dispositivo cuando el stock decae. | 1. Localizar un producto con cantidad ≥ 5.<br>2. Disminuir stock manualmente a 4 o menos mediante el botón restar (`-`). | El sistema debe interceptar el cambio de la UI, ordenar la actualización a Room, y tras el éxito, lanzar una Notificación Push local de "Stock Bajo" y pintar la tarjeta en rojo/color de alerta. |
| **INT-03** | **Exportación cruzada a UI y Fichero:** El `PdfGenerator` lee de Room y guarda en Storage físico. | 1. Entrar en pantalla Inventario.<br>2. Apretar `FloatingActionButton` de Generar PDF.<br>3. Acceder al visor de reportes. | Se genera archivo `.pdf` en Directorio Interno, salta "Notificación de Éxito", y el componente `PdfRenderer` es capaz de parsearlo y visualizarlo desde la nueva pestaña interna. |

### 2.2. Pruebas de Regresión
**Objetivo:** Confirmar que las refactorizaciones, cambios arquitectónicos y mejoras recientes de UI implementadas (Paso a Material 3 Premium, Soporte Color Dinámico y Cambios DAO) no han corrompido funciones preexistentes que operaban correctamente.

| ID Prueba | Descripción del Escenario | Pasos de Ejecución | Resultado Esperado |
|:---:|---|---|---|
| **REG-01** | **Inyección de Dependencias Hilt:** Asegurar que los módulos no colapsan en tiempo de ejecución tras cambiar `InventoyScreen` y `SettingsScreen`. | 1. Abrir la App.<br>2. Transitar rápidamente entre los 4 apartados de la `BottomNavigationBar`. | La App no lanza errores de `UninitializedPropertyAccessException` y los ViewModels retienen el estado. |
| **REG-02** | **Formularios de Configuraciones:** Corroborar que el arreglo local de estados previene sobrescribir en DataStore por cada pulsación de letra (race condition). | 1. Entrar a Ajustes e introducir "M" y rápidamente "i Tienda".<br>2. Tipear y salir del foco. | Ninguna letra desaparece. El campo de texto reacciona instantáneamente sin "saltos" y el proceso asíncrono DataStore finaliza detrás de escena solo al terminar. |

### 2.3. Pruebas de Uso de Recursos (Rendimiento)
**Objetivo:** Evaluar cómo responde el dispositivo frente al consumo de memoria, CPU y red (NetworkMonitor) gestionado por las Corrutinas de Kotlin.

| ID Prueba | Descripción del Escenario | Pasos de Ejecución | Resultado Esperado |
|:---:|---|---|---|
| **RES-01** | **Consumo de Memoria de PdfRenderer:** Analizar la respuesta del Garbage Collector manejando Bitmaps. | 1. Generar 3 PDFs extensos en la app.<br>2. Ir a la pestaña Reportes.<br>3. Abrir los PDFs y scrollear rápidamente. | La aplicación debe reciclar Bitmaps fluidamente (`page.close()`) y el OS no debe matar la aplicación por `OutOfMemoryError`. |
| **RES-02** | **Gráficos Core UI (Canvas):** Renderizado del Componente Donut Animado. | 1. Cambiar a la pestaña "Panel".<br>2. Observar la fluidez y FPS del gráfico. | Transición en 60 fotogramas sin bloquear o paralizar el hilo principal de la UI (`Dispatchers.Main`). |

### 2.4. Pruebas de Seguridad
**Objetivo:** Proteger la descarga de datos externos maliciosos o despilfarro de red local, garantizando privacidad en memorias.

| ID Prueba | Descripción del Escenario | Pasos de Ejecución | Resultado Esperado |
|:---:|---|---|---|
| **SEG-01** | **Verificación Estricta Wi-Fi:** Asegurar protección contra redes no autorizadas. | 1. Desconectar de la Red SSID de Tienda o estar en Datos 4G.<br>2. Intentar pulsar "Descargar". | El puente de `InventoryViewModel` rechaza instantáneamente la conexión, denegando el gasto de datos y mostrando error "Configura SSID... / WiFI no coincide". |
| **SEG-02** | **Protección de Runtime Permissions:** Evitar "Unknown SSID" exploit desactivando la geolocalización. | 1. Apagar el servicio de GPS (Ubicación) en el smartphone pero mantener WiFi conectado.<br>2. Darle a sincronizar. | Ante la lectura abstracta de `<unknown ssid>` que eluden dispositivos >= Android 8 por motivos de privacidad, la App aborta y devuelve "Falso" por seguridad, forzando la aprobación de ubicación explicita del usuario para certificar el SSID real. |

### 2.5. Pruebas Automatizadas (Unit & UI Testing)
**Objetivo:** Asegurar la ausencia de fallos humanos validando los flujos de código mediante la API de testing de Android (JUnit y Compose UI).

| ID Prueba | Descripción del Escenario | Pasos de Ejecución | Resultado Esperado |
|:---:|---|---|---|
| **AUT-01** | **Persistencia In-Memory (Room):** Comprobar inserción del `ProductDao`. | Ejecutar `ProductDaoTest.kt` alojado en `androidTest`. El Test levanta BDD temporal insertando producto Dummy. | El Test (`assertEquals`) confirma que el DAO recupera exactamente lo recién insertado devolviendo `PASSED`. |
| **AUT-02** | **Aislamiento MVVM (ViewModel):** Simular el repositorio de datos backend. | Ejecutar `InventoryViewModelTest.kt`. El framework inyecta un Repo Falso usando `kotlinx-coroutines-test` y modifica el Stock. | El `StateFlow` reactivo detecta la actualización sin crashear el motor, validado con `assertEquals` retornando `PASSED`. |
| **AUT-03** | **Jerarquía Semántica UI (Compose):** Simular el renderizado de Pantalla. | Ejecutar `InventoryScreenTest.kt`. Se expone el Componente Visual UI aislado. | Compose escanea su propio árbol y `assertIsDisplayed` encuentra el Título y Cantidad en pantalla retornando `PASSED`. |
