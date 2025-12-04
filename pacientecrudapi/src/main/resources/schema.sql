-- src/main/resources/schema.sql
-- NOTA: La base de datos 'clinica_db' debe ser creada manualmente en MySQL antes de ejecutar la aplicación.

-- Creamos la tabla 'paciente
CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dui VARCHAR(10) NOT NULL UNIQUE,
    primer_nombre VARCHAR(12) NOT NULL,
    apellido VARCHAR(12) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    tipo_sangre VARCHAR(5) NOT NULL
    );
