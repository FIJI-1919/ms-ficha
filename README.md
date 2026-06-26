# Microservicio Ficha Clínica - VetNova

Microservicio encargado de administrar las fichas clínicas veterinarias del sistema VetNova.

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Spring WebClient
* MySQL
* Maven
* Lombok
* JUnit 5
* Mockito

## Funcionalidades

* Listar fichas clínicas.
* Buscar ficha clínica por ID.
* Registrar fichas clínicas.
* Actualizar fichas clínicas.
* Eliminar fichas clínicas.
* Validar la existencia de citas mediante comunicación con el microservicio ms-agenda.

## Puerto utilizado

8085

## Base de datos

fichas_db

## Endpoints principales

### Listar fichas clínicas

GET /api/v1/fichas

### Buscar ficha clínica por ID

GET /api/v1/fichas/{id}

### Registrar ficha clínica

POST /api/v1/fichas

### Actualizar ficha clínica

PUT /api/v1/fichas/{id}

### Eliminar ficha clínica

DELETE /api/v1/fichas/{id}

## Validaciones implementadas

* ID de la cita obligatorio.
* Diagnóstico obligatorio.
* Tratamiento obligatorio.
* Veterinario obligatorio.
* Validación de existencia de la cita mediante WebClient.
* Validaciones mediante Bean Validation.

## Manejo de excepciones

* Ficha clínica no encontrada.
* Cita no encontrada.
* Error de comunicación con ms-agenda.
* Errores de validación.
* Error interno del servidor.

## Logs implementados

El microservicio registra:

* Listado de fichas clínicas.
* Búsquedas por ID.
* Registro de fichas clínicas.
* Actualización de fichas clínicas.
* Eliminación de fichas clínicas.
* Validación de citas.
* Errores del sistema.

## Pruebas unitarias

Se implementaron pruebas unitarias para:

* Controlador (Controller)
* Servicio (Service)

Resultado:

* 10 pruebas ejecutadas.
* 0 fallos.

## Ejecución del proyecto

```bash
mvn spring-boot:run
```

## Autor

Adriano Contreras
