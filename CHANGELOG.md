# Changelog - App Sensorial

## [2.0] - 2026-09-14

### Añadido
- **Programación Orientada a Objetos (POO):** Implementación de la clase `UserRepository` para separar la capa de datos y manejar la lógica de registro y autenticación.
- **Funciones de Extensión y Manejo de Errores:** Creación de `AudioUtils.kt` con la función extendida `Context.iniciarReconocimientoSeguro`, incorporando un bloque `try-catch` puro para evitar cierres inesperados al fallar el Speech-to-Text (sin conexión o incompatibilidad).
- **Funciones de Orden Superior:** Uso de lambdas pasadas como parámetros para controlar los eventos de éxito y error en la inicialización del micrófono.

### Modificado
- **Control de Usuarios:** Aplicación de funciones de orden superior (`.any {}`) en el repositorio para garantizar el límite estricto de 5 usuarios y evitar registros duplicados.
- **Validación de Recuperación:** Actualización de `PassRecoveryView` para validar la existencia real del usuario en el repositorio antes de simular el envío del correo.
- **Documentación Técnica:** Ampliación detallada del Diagrama y Diccionario EDT, junto con la evidencia explícita de las mitigaciones de riesgos en la arquitectura del código.

## [1.0] - 2026-08-22

### Añadido
- **Navegación:** Implementación de Jetpack Compose Navigation (`NavHost`, `NavController`) para gestionar el historial y las transiciones entre Login, Registro, Recuperación y Comunicador.
- **Accesibilidad (TTS):** Integración del motor nativo Text-to-Speech para la reproducción de texto a voz desde el tablero de comunicación.
- **Accesibilidad (STT):** Integración de reconocimiento de voz mediante `RecognizerIntent` de Google para la transcripción de dictado a texto.
- **Componentes UI:** Diseño con Material Design 3 utilizando `LazyVerticalGrid` para el tablero de frases rápidas, botones, *checkbox*, *radio buttons* y menús desplegables (`ExposedDropdownMenuBox`).
- **Validaciones de Seguridad:** El registro exige para la contraseña un mínimo de 6 caracteres, al menos una mayúscula, una minúscula y un número.
- **Usabilidad (UX):** Funcionalidad de alternar la visibilidad de contraseñas (ícono de ojo) e implementación de teclados inteligentes (`KeyboardOptions` con acciones *Next* y *Done*).
- **Formularios Dinámicos:** Lógica condicional en el registro que oculta el selector de nivel auditivo automáticamente si se selecciona el perfil "Usuario Oyente".

### Optimizado
- **Adaptabilidad Responsiva:** Incorporación de `verticalScroll` y estructuración de `GridItemSpan` para prevenir cortes de interfaz al rotar el dispositivo (modo *landscape*) o al usar pantallas pequeñas.
- **Gestión de Memoria:** Liberación correcta de los recursos del motor de voz (`tts.shutdown()`) al destruir la vista del comunicador utilizando `DisposableEffect` para prevenir fugas de memoria.