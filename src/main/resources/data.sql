-- =============================================
-- PurpleForce Gym - Datos de ejemplo (seed)
-- =============================================
-- Este script se ejecuta automáticamente al arrancar la aplicación.
-- Solo inserta datos si las tablas están vacías.

-- USUARIOS (contraseñas cifradas con BCrypt - todas son "1234")
INSERT INTO usuario (nombre, apellidos, email, password, rol, activo, fecha_alta)
SELECT 'Admin', 'PurpleForce', 'admin@purpleforce.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RIZBqMYFq9MCMIdte',
       'ADMIN', true, '2025-09-01'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@purpleforce.com');

INSERT INTO usuario (nombre, apellidos, email, password, rol, activo, fecha_alta)
SELECT 'Carlos', 'Martínez López', 'carlos@purpleforce.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RIZBqMYFq9MCMIdte',
       'INSTRUCTOR', true, '2025-09-01'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'carlos@purpleforce.com');

INSERT INTO usuario (nombre, apellidos, email, password, rol, activo, fecha_alta)
SELECT 'Laura', 'Gómez Pérez', 'laura@purpleforce.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RIZBqMYFq9MCMIdte',
       'INSTRUCTOR', true, '2025-09-15'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'laura@purpleforce.com');

INSERT INTO usuario (nombre, apellidos, email, password, rol, activo, fecha_alta)
SELECT 'Alejandro', 'Fernández', 'alex@gmail.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RIZBqMYFq9MCMIdte',
       'SOCIO', true, '2025-10-01'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'alex@gmail.com');

INSERT INTO usuario (nombre, apellidos, email, password, rol, activo, fecha_alta)
SELECT 'María', 'Rodríguez Díaz', 'maria@gmail.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RIZBqMYFq9MCMIdte',
       'SOCIO', true, '2025-10-10'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'maria@gmail.com');

INSERT INTO usuario (nombre, apellidos, email, password, rol, activo, fecha_alta)
SELECT 'Javier', 'Sánchez Ruiz', 'javier@gmail.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RIZBqMYFq9MCMIdte',
       'SOCIO', true, '2025-11-01'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'javier@gmail.com');

-- ENTRENADORES
INSERT INTO entrenador (nombre, apellidos, especialidad, telefono, email, activo)
SELECT 'Carlos', 'Martínez López', 'Musculación y Fuerza', '612345678', 'carlos@purpleforce.com', true
WHERE NOT EXISTS (SELECT 1 FROM entrenador WHERE email = 'carlos@purpleforce.com');

INSERT INTO entrenador (nombre, apellidos, especialidad, telefono, email, activo)
SELECT 'Laura', 'Gómez Pérez', 'Yoga y Pilates', '698765432', 'laura@purpleforce.com', true
WHERE NOT EXISTS (SELECT 1 FROM entrenador WHERE email = 'laura@purpleforce.com');

INSERT INTO entrenador (nombre, apellidos, especialidad, telefono, email, activo)
SELECT 'Miguel', 'Torres Blanco', 'Cardio y HIIT', '677123456', 'miguel@purpleforce.com', true
WHERE NOT EXISTS (SELECT 1 FROM entrenador WHERE email = 'miguel@purpleforce.com');

-- TIPOS DE CLASE
INSERT INTO tipo_clase (nombre, descripcion, nivel, duracion_minutos, precio, color)
SELECT 'Yoga', 'Clases de yoga para relajación y flexibilidad', 'PRINCIPIANTE', 60, 12.00, '#9b59b6'
WHERE NOT EXISTS (SELECT 1 FROM tipo_clase WHERE nombre = 'Yoga');

INSERT INTO tipo_clase (nombre, descripcion, nivel, duracion_minutos, precio, color)
SELECT 'Musculación', 'Entrenamiento de fuerza y hipertrofia muscular', 'AVANZADO', 75, 15.00, '#6c3483'
WHERE NOT EXISTS (SELECT 1 FROM tipo_clase WHERE nombre = 'Musculación');

INSERT INTO tipo_clase (nombre, descripcion, nivel, duracion_minutos, precio, color)
SELECT 'HIIT', 'High Intensity Interval Training para quemar calorías', 'INTERMEDIO', 45, 14.00, '#8e44ad'
WHERE NOT EXISTS (SELECT 1 FROM tipo_clase WHERE nombre = 'HIIT');

INSERT INTO tipo_clase (nombre, descripcion, nivel, duracion_minutos, precio, color)
SELECT 'Pilates', 'Control y fortalecimiento del core corporal', 'PRINCIPIANTE', 60, 12.00, '#a569bd'
WHERE NOT EXISTS (SELECT 1 FROM tipo_clase WHERE nombre = 'Pilates');

INSERT INTO tipo_clase (nombre, descripcion, nivel, duracion_minutos, precio, color)
SELECT 'Spinning', 'Ciclismo indoor de alta intensidad', 'INTERMEDIO', 50, 13.00, '#7d3c98'
WHERE NOT EXISTS (SELECT 1 FROM tipo_clase WHERE nombre = 'Spinning');

INSERT INTO tipo_clase (nombre, descripcion, nivel, duracion_minutos, precio, color)
SELECT 'Zumba', 'Baile fitness de ritmos latinos', 'PRINCIPIANTE', 60, 11.00, '#bb8fce'
WHERE NOT EXISTS (SELECT 1 FROM tipo_clase WHERE nombre = 'Zumba');

-- EJERCICIOS
INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Sentadilla', 'Ejercicio básico de piernas y glúteos', 'PIERNAS', 'BASICO', 'Ninguno'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Sentadilla');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Press de banca', 'Ejercicio principal de pecho con barra', 'PECHO', 'INTERMEDIO', 'Barra y banco'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Press de banca');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Dominadas', 'Ejercicio de tracción vertical para espalda', 'ESPALDA', 'AVANZADO', 'Barra de dominadas'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Dominadas');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Peso muerto', 'Ejercicio compuesto para espalda y piernas', 'ESPALDA', 'AVANZADO', 'Barra y discos'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Peso muerto');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Plancha', 'Ejercicio isométrico para el core', 'CORE', 'BASICO', 'Ninguno'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Plancha');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Curl de bíceps', 'Ejercicio de aislamiento para bíceps', 'BRAZOS', 'BASICO', 'Mancuernas'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Curl de bíceps');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Burpees', 'Ejercicio cardiovascular de cuerpo completo', 'CUERPO_COMPLETO', 'INTERMEDIO', 'Ninguno'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Burpees');

INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, dificultad, material_necesario)
SELECT 'Mountain Climbers', 'Ejercicio cardiovascular para core y piernas', 'CORE', 'INTERMEDIO', 'Ninguno'
WHERE NOT EXISTS (SELECT 1 FROM ejercicio WHERE nombre = 'Mountain Climbers');

-- CLASES (sesiones concretas)
-- Usamos SELECT para obtener IDs dinámicamente
INSERT INTO clase (tipo_clase_id, entrenador_id, fecha, hora_inicio, hora_fin, aforo_maximo, sala, activa)
SELECT tc.id, e.id, '2026-06-09', '09:00:00', '10:00:00', 15, 'Sala A', true
FROM tipo_clase tc, entrenador e
WHERE tc.nombre = 'Yoga' AND e.email = 'laura@purpleforce.com'
AND NOT EXISTS (SELECT 1 FROM clase c2
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    WHERE tc2.nombre='Yoga' AND c2.fecha='2026-06-09' AND c2.hora_inicio='09:00:00');

INSERT INTO clase (tipo_clase_id, entrenador_id, fecha, hora_inicio, hora_fin, aforo_maximo, sala, activa)
SELECT tc.id, e.id, '2026-06-09', '10:30:00', '11:15:00', 12, 'Sala B', true
FROM tipo_clase tc, entrenador e
WHERE tc.nombre = 'HIIT' AND e.email = 'miguel@purpleforce.com'
AND NOT EXISTS (SELECT 1 FROM clase c2
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    WHERE tc2.nombre='HIIT' AND c2.fecha='2026-06-09' AND c2.hora_inicio='10:30:00');

INSERT INTO clase (tipo_clase_id, entrenador_id, fecha, hora_inicio, hora_fin, aforo_maximo, sala, activa)
SELECT tc.id, e.id, '2026-06-09', '17:00:00', '18:15:00', 10, 'Sala Pesas', true
FROM tipo_clase tc, entrenador e
WHERE tc.nombre = 'Musculación' AND e.email = 'carlos@purpleforce.com'
AND NOT EXISTS (SELECT 1 FROM clase c2
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    WHERE tc2.nombre='Musculación' AND c2.fecha='2026-06-09' AND c2.hora_inicio='17:00:00');

INSERT INTO clase (tipo_clase_id, entrenador_id, fecha, hora_inicio, hora_fin, aforo_maximo, sala, activa)
SELECT tc.id, e.id, '2026-06-10', '09:30:00', '10:30:00', 20, 'Sala A', true
FROM tipo_clase tc, entrenador e
WHERE tc.nombre = 'Zumba' AND e.email = 'laura@purpleforce.com'
AND NOT EXISTS (SELECT 1 FROM clase c2
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    WHERE tc2.nombre='Zumba' AND c2.fecha='2026-06-10' AND c2.hora_inicio='09:30:00');

INSERT INTO clase (tipo_clase_id, entrenador_id, fecha, hora_inicio, hora_fin, aforo_maximo, sala, activa)
SELECT tc.id, e.id, '2026-06-10', '11:00:00', '11:50:00', 15, 'Sala Spinning', true
FROM tipo_clase tc, entrenador e
WHERE tc.nombre = 'Spinning' AND e.email = 'miguel@purpleforce.com'
AND NOT EXISTS (SELECT 1 FROM clase c2
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    WHERE tc2.nombre='Spinning' AND c2.fecha='2026-06-10' AND c2.hora_inicio='11:00:00');

INSERT INTO clase (tipo_clase_id, entrenador_id, fecha, hora_inicio, hora_fin, aforo_maximo, sala, activa)
SELECT tc.id, e.id, '2026-06-11', '19:00:00', '20:00:00', 18, 'Sala A', true
FROM tipo_clase tc, entrenador e
WHERE tc.nombre = 'Pilates' AND e.email = 'laura@purpleforce.com'
AND NOT EXISTS (SELECT 1 FROM clase c2
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    WHERE tc2.nombre='Pilates' AND c2.fecha='2026-06-11' AND c2.hora_inicio='19:00:00');

-- RESERVAS de ejemplo (alex y maria)
INSERT INTO reserva (clase_id, usuario_id, fecha_reserva, estado)
SELECT c.id, u.id, NOW(), 'CONFIRMADA'
FROM clase c
JOIN tipo_clase tc ON c.tipo_clase_id = tc.id
JOIN usuario u ON u.email = 'alex@gmail.com'
WHERE tc.nombre = 'Yoga' AND c.fecha = '2026-06-09'
AND NOT EXISTS (
    SELECT 1 FROM reserva r2
    JOIN clase c2 ON r2.clase_id = c2.id
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    JOIN usuario u2 ON r2.usuario_id = u2.id
    WHERE tc2.nombre='Yoga' AND c2.fecha='2026-06-09' AND u2.email='alex@gmail.com'
);

INSERT INTO reserva (clase_id, usuario_id, fecha_reserva, estado)
SELECT c.id, u.id, NOW(), 'CONFIRMADA'
FROM clase c
JOIN tipo_clase tc ON c.tipo_clase_id = tc.id
JOIN usuario u ON u.email = 'alex@gmail.com'
WHERE tc.nombre = 'HIIT' AND c.fecha = '2026-06-09'
AND NOT EXISTS (
    SELECT 1 FROM reserva r2
    JOIN clase c2 ON r2.clase_id = c2.id
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    JOIN usuario u2 ON r2.usuario_id = u2.id
    WHERE tc2.nombre='HIIT' AND c2.fecha='2026-06-09' AND u2.email='alex@gmail.com'
);

INSERT INTO reserva (clase_id, usuario_id, fecha_reserva, estado)
SELECT c.id, u.id, NOW(), 'CONFIRMADA'
FROM clase c
JOIN tipo_clase tc ON c.tipo_clase_id = tc.id
JOIN usuario u ON u.email = 'maria@gmail.com'
WHERE tc.nombre = 'Yoga' AND c.fecha = '2026-06-09'
AND NOT EXISTS (
    SELECT 1 FROM reserva r2
    JOIN clase c2 ON r2.clase_id = c2.id
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    JOIN usuario u2 ON r2.usuario_id = u2.id
    WHERE tc2.nombre='Yoga' AND c2.fecha='2026-06-09' AND u2.email='maria@gmail.com'
);

INSERT INTO reserva (clase_id, usuario_id, fecha_reserva, estado)
SELECT c.id, u.id, NOW(), 'CONFIRMADA'
FROM clase c
JOIN tipo_clase tc ON c.tipo_clase_id = tc.id
JOIN usuario u ON u.email = 'maria@gmail.com'
WHERE tc.nombre = 'Zumba' AND c.fecha = '2026-06-10'
AND NOT EXISTS (
    SELECT 1 FROM reserva r2
    JOIN clase c2 ON r2.clase_id = c2.id
    JOIN tipo_clase tc2 ON c2.tipo_clase_id = tc2.id
    JOIN usuario u2 ON r2.usuario_id = u2.id
    WHERE tc2.nombre='Zumba' AND c2.fecha='2026-06-10' AND u2.email='maria@gmail.com'
);
