# Changelog - App Sensorial

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