-- 1. Deshabilitar el modo Safe Update (solo para esta sesión)
--    Esto evita el error "You are using safe update mode" al hacer DELETE o UPDATE sin WHERE.
SET SQL_SAFE_UPDATES = 0;

-- 2. Eliminar la base de datos si ya existe (opcional, con cuidado)
DROP DATABASE IF EXISTS gimnasio_db;

-- 3. Crear la base de datos nueva
CREATE DATABASE gimnasio_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. Usar la base de datos
USE gimnasio_db;

-- CREACIÓN DE TABLAS (orden según dependencias)

-- Tabla de usuarios (autenticación)
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'INSTRUCTOR', 'SOCIO') NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de instructores (extiende a usuarios)
CREATE TABLE instructor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    especialidad VARCHAR(255),
    biografia TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de tipos de clase
CREATE TABLE tipos_clase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nivel VARCHAR(50),
    descripcion TEXT
);

-- Tabla de equipamiento
CREATE TABLE equipamiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cantidad_total INT NOT NULL,
    estado VARCHAR(50)
);

-- Tabla de clases (con control de concurrencia mediante campo version)
CREATE TABLE clase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    tipo_id BIGINT,
    instructor_id BIGINT,
    hora_inicio DATETIME NOT NULL,
    duracion_minutos INT NOT NULL,
    aforo_maximo INT NOT NULL,
    plazas_disponibles INT NOT NULL,
    version INT DEFAULT 0,
    FOREIGN KEY (tipo_id) REFERENCES tipos_clase(id) ON DELETE SET NULL,
    FOREIGN KEY (instructor_id) REFERENCES instructor(id) ON DELETE SET NULL
);

-- Tabla de relación muchos a muchos entre clase y equipamiento
CREATE TABLE clase_equipamiento (
    clase_id BIGINT NOT NULL,
    equipamiento_id BIGINT NOT NULL,
    cantidad_necesaria INT DEFAULT 1,
    PRIMARY KEY (clase_id, equipamiento_id),
    FOREIGN KEY (clase_id) REFERENCES clase(id) ON DELETE CASCADE,
    FOREIGN KEY (equipamiento_id) REFERENCES equipamiento(id) ON DELETE CASCADE
);

-- Tabla de reservas
CREATE TABLE reserva (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    clase_id BIGINT NOT NULL,
    fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'confirmada',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clase(id) ON DELETE CASCADE
);

-- =============================================
-- INSERCIÓN DE DATOS DE EJEMPLO (FICTICIOS)
-- =============================================

-- Insertar usuarios (contraseña en texto plano, luego la encriptarás con BCrypt en la app)
-- Para pruebas: password = "1234" (sin encriptar, pero en la app usarás encoder)
INSERT INTO usuarios (email, nombre, password, rol) VALUES
('admin@gimnasio.com', 'Admin Principal', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN'),
('ana.instructora@gimnasio.com', 'Ana López', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'INSTRUCTOR'),
('carlos.socio@gimnasio.com', 'Carlos Ruiz', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'SOCIO'),
('laura.socio2@gimnasio.com', 'Laura Gómez', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'SOCIO');

-- Nota: Las contraseñas están hasheadas con BCrypt (para "1234"). Si quieres usar otras, genera los hashes con la app.

-- Insertar instructores
INSERT INTO instructor (usuario_id, especialidad, biografia) VALUES
(2, 'Yoga y Pilates', 'Instructora certificada con 5 años de experiencia.');

-- Insertar tipos de clase
INSERT INTO tipos_clase (nombre, nivel, descripcion) VALUES
('Yoga', 'Principiante', 'Clase suave para mejorar flexibilidad y reducir estrés.'),
('Yoga', 'Avanzado', 'Posturas complejas y mayor exigencia física.'),
('Crossfit', 'Intermedio', 'Entrenamiento de alta intensidad por intervalos.'),
('Spinning', 'Todos los niveles', 'Ciclo indoor con música motivadora.');

-- Insertar equipamiento
INSERT INTO equipamiento (nombre, cantidad_total, estado) VALUES
('Esterilla', 25, 'Bueno'),
('Pesa rusa 8kg', 10, 'Bueno'),
('Cinta de correr', 5, 'Regular'),
('Bicicleta de spinning', 12, 'Bueno');

-- Insertar clases
-- La clase con id 1 tiene aforo 2 para poder probar concurrencia fácilmente
INSERT INTO clase (titulo, tipo_id, instructor_id, hora_inicio, duracion_minutos, aforo_maximo, plazas_disponibles, version) VALUES
('Yoga Relajante', 1, 1, '2025-05-10 09:00:00', 60, 2, 2, 0),
('Crossfit Intenso', 3, 1, '2025-05-10 11:00:00', 45, 8, 8, 0),
('Spinning Tormenta', 4, 1, '2025-05-11 18:00:00', 50, 10, 10, 0);

-- Relacionar clases con equipamiento necesario
INSERT INTO clase_equipamiento (clase_id, equipamiento_id, cantidad_necesaria) VALUES
(1, 1, 1),   -- Yoga necesita esterilla
(2, 2, 2),   -- Crossfit necesita pesas rusas (2 por persona)
(2, 3, 1),   -- Crossfit necesita cinta
(3, 4, 1);   -- Spinning necesita bicicleta

-- Insertar algunas reservas de ejemplo (estado 'confirmada')
INSERT INTO reserva (usuario_id, clase_id, estado) VALUES
(3, 1, 'confirmada'),   -- Carlos reserva yoga
(4, 2, 'confirmada');   -- Laura reserva crossfit