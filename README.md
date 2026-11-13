# Laboratorio 13: Animaciones en Jetpack Compose

Programación en Móviles (Tecsup) · Sharon Jennifer Trujillo Sulca · Grupo D · Ciclo 4

App con un menú y una pantalla por ejercicio:

| Ejercicio | Tema | Tag | Video |
|---|---|---|---|
| 1 | `AnimatedVisibility` con `fadeIn`/`fadeOut` (y `expandVertically`/`shrinkVertically`) | `ejercicio1` | [ver](https://drive.google.com/file/d/1XTeUlhjF6eK33l4aqcqQd378X2BuhpZv/view?usp=sharing) |
| 2 | `animateColorAsState` con `tween` y `spring` | `ejercicio2` | [ver](https://drive.google.com/file/d/1FQeaPQHRN0lQytJi9KVt4ZFu6sqdvq5z/view?usp=sharing) |
| 3 | `animateDpAsState` + `Modifier.offset` y el orden de los modificadores | `ejercicio3` | [ver](https://drive.google.com/file/d/1DRRHe0gSkPV-QlDEZ48IlEON0SeXacai/view?usp=sharing) |
| 4 | `AnimatedContent` (Cargando, Contenido y Error) | `ejercicio4` | [ver](https://drive.google.com/file/d/1EUa21vICmWUchQ5arJvgIlKB54YVRnVC/view?usp=sharing) |
| Final | **Torre de gatitos**: juego de apilar gatitos echados apuntando al centro | `ejercicio-final` | [ver](https://drive.google.com/file/d/10pYPTlsXxqxU2PgMKABmPHi2peUGlAFr/view?usp=sharing) |

## Torre de gatitos

Un gatito se desliza de lado a lado y se suelta tocando la pantalla. Hay que apuntar al centro de la torre:

- a menos de 10 dp del centro: acierto **perfecto** (se acomoda al centro, +20 puntos y combo);
- a menos de 48 dp: queda un poco chueco (+10 puntos);
- más lejos: se resbala y se pierde una de las 3 vidas.

Cada gatito sale más rápido y el cielo cambia cada 6 gatitos. Los gatitos están dibujados con `Canvas`.

## Cómo correrlo

Abrir la carpeta en Android Studio y ejecutar el módulo `app`. Los videos se grabaron con pruebas de Robolectric + Roborazzi (`app/src/test`), no con el emulador.
