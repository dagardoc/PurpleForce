# 💪 PurpleForce Gym

> Aplicación web de gestión de gimnasio — Proyecto Intermodular 2º DAM  
> **Autor:** Daniel García Docío · **Curso:** 2025/26 · **Centro:** Nortempo Formación

---

## 📋 Descripción

**PurpleForce Gym** es una aplicación web completa para gestionar un gimnasio.  
Permite a administradores, instructores y socios interactuar con el sistema según su rol:

- **Administrador** → gestión total (clases, entrenadores, socios, ejercicios, informes)
- **Instructor** → ver sus clases asignadas y los alumnos inscritos
- **Socio** → explorar clases, hacer reservas, ver historial y descargar PDF

## 🛠️ Tecnologías

| Capa | Tecnología |
|------|-----------|
| Backend | Java 17 + Spring Boot 3.2 |
| Seguridad | Spring Security 6 (BCrypt, sesiones) |
| Persistencia | Spring Data JPA + MySQL 8 |
| Frontend | Thymeleaf + HTML/CSS + Bootstrap Icons |
| Informes | OpenPDF (PDF) + Apache Commons CSV |
| Tests | JUnit 5 + Spring Test + H2 (in-memory) |
| Control versiones | Git + GitHub |

---

## ⚙️ Requisitos previos

- **Java 17+** (JDK)  
- **Maven 3.8+**  
- **MySQL 8+** en ejecución  
- **IntelliJ IDEA** u otro IDE (recomendado)

---

## 🚀 Instalación y arranque

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/purpleforce-gym.git
cd purpleforce-gym
```

### 2. Crear la base de datos en MySQL

```sql
CREATE DATABASE purpleforce_gym CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar credenciales de base de datos

Edita `src/main/resources/application.properties` o usa variables de entorno:

```properties
# Opción A: editar directamente
spring.datasource.username=root
spring.datasource.password=tu_contraseña

# Opción B: variables de entorno (recomendado)
# DB_USER=root
# DB_PASS=tu_contraseña
```

### 4. Compilar y arrancar

```bash
mvn spring-boot:run
```

La aplicación arranca en **http://localhost:8080**

---

## 📦 Datos de ejemplo

Los datos de prueba se cargan **automáticamente** al arrancar por primera vez mediante el fichero `src/main/resources/data.sql`.

### Usuarios disponibles (contraseña de todos: `1234`)

| Rol | Email |
|-----|-------|
| Admin | `admin@purpleforce.com` |
| Instructor | `carlos@purpleforce.com` |
| Instructor | `laura@purpleforce.com` |
| Socio | `alex@gmail.com` |
| Socio | `maria@gmail.com` |

---

## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/com/purpleforce/gym/
│   │   ├── GymApplication.java          ← Clase principal
│   │   ├── config/
│   │   │   └── SecurityConfig.java      ← Spring Security
│   │   ├── controller/
│   │   │   ├── AdminController.java     ← Panel administrador
│   │   │   ├── SocioController.java     ← Panel socio
│   │   │   ├── InstructorController.java← Panel instructor
│   │   │   └── AuthController.java      ← Login/Registro
│   │   ├── model/
│   │   │   ├── Usuario.java
│   │   │   ├── Entrenador.java
│   │   │   ├── TipoClase.java
│   │   │   ├── Clase.java
│   │   │   ├── Ejercicio.java
│   │   │   ├── Reserva.java
│   │   │   └── Rol.java
│   │   ├── repository/                  ← Interfaces JPA
│   │   └── service/                     ← Lógica de negocio
│   └── resources/
│       ├── templates/                   ← Vistas Thymeleaf
│       ├── static/css/style.css         ← Estilos morado/blanco/negro
│       ├── application.properties
│       └── data.sql                     ← Datos de ejemplo
└── test/
    └── GymApplicationTests.java         ← 10 pruebas funcionales
```

---

## 🧪 Ejecutar los tests

```bash
mvn test
```

Los tests usan H2 en memoria, no necesitan MySQL activo.

---

## 📊 Funcionalidades principales

- ✅ Login/registro con roles (ADMIN, INSTRUCTOR, SOCIO)
- ✅ CRUD completo de clases, entrenadores, tipos de clase y ejercicios
- ✅ Reserva y cancelación de clases con control de aforo en tiempo real
- ✅ Control de concurrencia mediante `@Transactional`
- ✅ Ranking de clases más populares (consulta no trivial 1)
- ✅ Historial de reservas por usuario (consulta no trivial 2)
- ✅ Exportar historial en **PDF** (OpenPDF)
- ✅ Exportar ranking en **CSV** (Apache Commons CSV)
- ✅ Filtros combinados (nivel + fecha)
- ✅ Datos de ejemplo cargados automáticamente

---

## 📝 Memoria del proyecto

La memoria final en PDF está en: `/docs/Memoria_GarciaDaniel.pdf`
