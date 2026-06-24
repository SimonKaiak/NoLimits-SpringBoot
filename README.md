# NoLimits - Backend Spring Boot

Proyecto de backend desarrollado con Spring Boot para la plataforma NoLimits, un sistema de gestión de productos multimedia (videojuegos, películas, series, animes, libros, música) con funcionalidades de ventas, reseñas, usuarios y un chatbot integrado con OpenAI.

Asignatura: Desarrollo de Aplicaciones Web  
Sección: 001D  
Profesor: Cristian Calderón

Integrantes:
- Marta Sanhueza
- James Videla
- Christian Troncoso

---

## Descripción general

La API expone endpoints REST versionados (v1 y v2) que cubren la administración de productos, catálogos de géneros, plataformas, desarrolladores y empresas, gestión de usuarios y roles, ventas, reseñas, ubicaciones geográficas (regiones y comunas), y servicios de integración con APIs externas como IGDB, TMDB, RAWG y Google Books.

Además incluye un sistema de búsqueda semántica mediante embeddings generados con OpenAI y almacenados en PostgreSQL con la extensión pgvector.

---

## Tecnologías usadas

- Java 17
- Spring Boot 3.3.13
- Spring Security con autenticación JWT (jjwt 0.11.5)
- Spring Data JPA con Hibernate
- PostgreSQL (producción) con extensión pgvector para embeddings
- H2 (base de datos en memoria para pruebas)
- Spring HATEOAS para respuestas hipermedia
- Springdoc OpenAPI 2.6 (Swagger UI)
- Lombok
- Datafaker para seed de datos
- OpenAI Java SDK 4.32.0
- Jacoco para cobertura de código
- Docker con build multi-stage (Maven 3.9.9 + Eclipse Temurin 21)
- Despliegue en Render

---

## Requisitos previos

- Java 17 o superior
- Maven 3.9+
- PostgreSQL con la extensión pgvector instalada
- Docker (opcional, para correr en contenedor)

---

## Variables de entorno

El proyecto requiere las siguientes variables de entorno configuradas antes de correr:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

SECURITY_JWT_SECRET
SECURITY_JWT_EXPIRATION   (opcional, por defecto 604800000 ms = 7 días)

ADMIN_EMAIL               (opcional, por defecto admin@nolimits.com)
ADMIN_PASSWORD            (opcional, por defecto admin123)

IGDB_CLIENT_ID
IGDB_CLIENT_SECRET
TMDB_TOKEN
RAWG_KEY
GOOGLE_BOOKS_KEY
OPENAI_API_KEY
PORT                      (opcional, por defecto 8080)
```

---

## Cómo correr el proyecto

Clonar el repositorio y pararse en la raíz del proyecto.

Con Maven directamente:

```bash
./mvnw spring-boot:run
```

Para construir el jar:

```bash
./mvnw clean package -DskipTests
java -jar target/NoLimits-0.0.1-SNAPSHOT.jar
```

Con Docker:

```bash
docker build -t nolimits-backend .
docker run -p 8080:8080 --env-file .env nolimits-backend
```

---

## Documentación de la API

Una vez levantada la aplicación, la documentación Swagger está disponible en:

```
http://localhost:8080/doc/swagger-ui.html
```

---

## Endpoints principales

La API está versionada. Los endpoints siguen la convención `/api/v1/...` y `/api/v2/...`.

Autenticación:
- POST /api/auth/login

Productos:
- GET, POST /api/v1/productos
- GET, PUT, DELETE /api/v1/productos/{id}

Ventas:
- GET, POST /api/v1/ventas
- GET, PUT, DELETE /api/v1/ventas/{id}

Usuarios y roles:
- GET, POST /api/v1/usuarios
- GET, POST /api/v1/roles

Catálogos (géneros, plataformas, clasificaciones, etc.):
- /api/v1/generos
- /api/v1/plataformas
- /api/v1/clasificaciones
- /api/v1/estados
- /api/v1/metodos-pago
- /api/v1/metodos-envio
- /api/v1/tipos-producto
- /api/v1/desarrolladores
- /api/v1/empresas

Ubicación:
- /api/v1/regiones
- /api/v1/comunas
- /api/v1/direcciones

Reseñas:
- GET, POST /api/v1/reviews

Chatbot:
- POST /api/chatbot

Integraciones externas (actúan como proxy):
- /api/igdb/...
- /api/tmdb/...
- /api/rawg/...
- /api/google-books/...

Health check:
- GET /health

---

## Tests

Para correr las pruebas:

```bash
./mvnw test
```

Para generar el reporte de cobertura con Jacoco:

```bash
./mvnw verify
```

El reporte queda en `target/site/jacoco/index.html`.

---

## Estructura del proyecto

```
src/
  main/
    java/com/example/NoLimits/Multimedia/
      chatbot/          chatbot con OpenAI
      config/           configuración de seguridad, roles y Swagger
      controller/       endpoints REST v1
      controllerV2/     endpoints REST v2
      dto/              objetos de transferencia de datos (request, response, update)
      model/            entidades JPA
      repository/       repositorios Spring Data
      service/          lógica de negocio
      assemblers/       assemblers HATEOAS
      _exceptions/      manejo global de errores
    resources/
      application.properties
      init-pgvector.sql
  test/
    java/               pruebas unitarias con JUnit y Mockito
```
