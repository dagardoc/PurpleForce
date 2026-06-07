# PurpleForce Gym

> Aplicación web de gestión de gimnasio — Proyecto Intermodular 2º DAM  
> **Autor:** Daniel García Docío · **Curso:** 2025/26 · **Centro:** Nortempo Formación

---

## Descripción

**PurpleForce Gym** es una aplicación web completa para gestionar un gimnasio.  
Permite a administradores, instructores y socios interactuar con el sistema según su rol:

- **Administrador** — gestión total (clases, entrenadores, socios, ejercicios, informes)
- **Instructor** — ver sus clases asignadas y los alumnos inscritos
- **Socio** — explorar clases, hacer reservas, ver historial y descargar PDF

---

## Tecnologías

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

## Requisitos previos

- Java 17 o superior (JDK, no solo JRE)
- Maven 3.8 o superior
- MySQL 8.0 en ejecución
- Visual Studio Code con Extension Pack for Java y Spring Boot Extension Pack

---

## Instalación y arranque

### 1. Clonar el repositorio

```bash
git clone https://github.com/dagardoc/PurpleForce.git
cd PurpleForce
```

### 2. Crear la base de datos en MySQL

```sql
CREATE DATABASE gimnasio_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar credenciales de base de datos

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gimnasio_db?useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

### 4. Arrancar la aplicación

Abre `src/main/java/com/purpleforce/gym/GymApplication.java` en VS Code y pulsa el botón **Run** que aparece encima del método `main`.

La aplicación arranca en **http://localhost:8080**

---

## Datos de ejemplo

Los datos de prueba se cargan **automáticamente** al arrancar mediante el fichero `src/main/resources/data.sql`.

### Usuarios disponibles (contraseña: `1234`)

| Rol | Email |
|-----|-------|
| Admin | `admin@purpleforce.com` |
| Instructor | `carlos@purpleforce.com` |
| Socio | `alex@gmail.com` |
| Socio | `maria@gmail.com` |

---

## Estructura del proyecto
