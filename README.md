# 🚀 Sistema de Gestión de Usuarios (CRUD en Memoria con JavaFX y CSS)

Aplicación de escritorio profesional desarrollada en **Java**, con interfaz gráfica moderna en **JavaFX**, estilos personalizados con **CSS** y gestión de datos **en memoria** mediante colecciones (`ArrayList<Usuario>`), sin persistencia en bases de datos ni archivos externos.

---

## 📁 Estructura del Proyecto

```text
CRUD USUARIOS JAVAFX/
├── pom.xml                                      # Configuración Maven y dependencias JavaFX
├── resources/
│   └── estilos.css                              # Hoja de estilos CSS personalizada
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── aplicacion/
│   │   │   │   ├── Main.java                    # Punto de entrada de la aplicación JavaFX
│   │   │   │   └── Launcher.java                # Lanzador compatible sin módulos
│   │   │   ├── controlador/
│   │   │   │   └── UsuarioController.java       # Lógica de negocio y ArrayList en memoria
│   │   │   ├── modelo/
│   │   │   │   └── Usuario.java                 # Entidad de datos
│   │   │   └── vista/
│   │   │       └── MainView.java                # Interfaz gráfica moderna y responsiva
│   │   └── resources/
│   │       └── estilos.css                      # Estilos empaquetados en classpath
│   └── test/
│       └── java/
│           └── controlador/
│               └── UsuarioControllerTest.java   # Pruebas unitarias automatizadas (JUnit 5)
```

---

## 🎨 Paleta de Colores Corporativa

| Color | Código Hex | Uso en la Aplicación |
| :--- | :--- | :--- |
| **Azul Marino** | `#1A237E` | Encabezados, títulos y bordes principales |
| **Azul Claro** | `#42A5F5` | Acentos, foco en campos de texto y selecciones |
| **Verde Éxito** | `#43A047` / `#66BB6A` | Botón **Guardar** y estados de confirmación |
| **Naranja Advertencia** | `#FB8C00` / `#FFA726` | Botón **Modificar** y estado de edición activa |
| **Rojo Peligro** | `#E53935` / `#EF5350` | Botón **Eliminar** y mensajes de error |
| **Gris / Neutro** | `#78909C` / `#ECEFF1` | Botón **Limpiar**, fondos y bordes secundarios |
| **Fondo General** | `#F5F5F5` | Fondo limpio de la ventana |
| **Texto Principal** | `#212121` | Contenido de campos y tablas |

---

## 🔄 Flujo de Datos

```
[Usuario ingresa datos] 
         │
         ▼
    [Formulario] 
         │  (Valida formato, campos obligatorios, correo único)
         ▼
   [Controller]
         │  (Añade / actualiza / elimina)
         ▼
[ArrayList en Memoria]
         │  (Sincroniza ObservableList reactiva)
         ▼
   [Tabla JavaFX] ──▶ (Actualización inmediata en pantalla)
```

---

## 🛠️ Requisitos de Ejecución

- **Java JDK:** Versión 21 o superior (compatible con OpenJDK 21, 22, 23 y 26).
- **Maven:** 3.8+ (o el Maven integrado en IntelliJ IDEA).

---

## ▶️ Instrucciones para Ejecutar

### Opción 1: Desde IntelliJ IDEA (Recomendada)
1. Abre el proyecto en **IntelliJ IDEA**.
2. Navega a `src/main/java/aplicacion/Main.java` (o `Launcher.java`).
3. Haz clic derecho y selecciona **Run 'Main.main()'** o presiona `Mayús + F10` (`Shift + F10`).

### Opción 2: Desde la Terminal con Maven
Abre una terminal o PowerShell en la carpeta raíz del proyecto y ejecuta:

```bash
mvn clean javafx:run
```

### Opción 3: Ejecutar las Pruebas Unitarias
Para verificar que todas las validaciones y operaciones CRUD funcionen al 100%:

```bash
mvn test
```
