# Paciente CRUD API - Spring Integration

Este proyecto es una demostración de cómo implementar una **API RESTful** utilizando **Spring Integration** como capa de control de flujo, en lugar de los controladores tradicionales de Spring MVC.

El sistema permite gestionar un directorio de **Pacientes** (CRUD completo) persistiendo la datos en una base de datos **MySQL**.

---

## Requisitos Previos

* **Java 17** o superior.
* **Maven** (para gestión de dependencias).
* **MySQL Server** (8.0 o superior).

---

## ⚙️ Configuración de la Base de Datos

El proyecto está configurado para conectarse a una base de datos local llamada `clinica_db`.

### 1. Crear la Base de Datos
Ejecutar el siguiente comando en tu gestor de base de datos (MySQL Workbench, consola, etc.):

```sql
CREATE DATABASE clinica_db;
```

## Tecnologías Utilizadas
- Spring Boot 3.3.12
- Spring Integration HTTP
- Spring Data JPA
- Lombok
- MySQL Connector

| MEtodo | URL | DescripciOn |
| :--- | :--- | :--- |
| **POST** | `/api/pacientes` | Crea un nuevo paciente. Requiere JSON en el cuerpo. |
| **GET** | `/api/pacientes` | Lista todos los pacientes registrados. |
| **GET** | `/api/pacientes/{dui}` | Obtiene el detalle de un paciente por su DUI. |
| **PUT** | `/api/pacientes/{dui}` | Actualiza los datos de un paciente existente. |
| **DELETE** | `/api/pacientes/{dui}` | Elimina un paciente del sistema. |
