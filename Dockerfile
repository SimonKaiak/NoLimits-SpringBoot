# ============================================================
# DOCKERFILE PARA BACKEND SPRING BOOT - NoLimits
# ============================================================


# ============================================================
# ETAPA 1: BUILD DEL PROYECTO CON MAVEN Y JAVA 21
# ============================================================
# Usamos una imagen oficial de Maven con Java 21 (Eclipse Temurin).
# Esta etapa se encarga de compilar tu proyecto y generar el .jar.
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo pom.xml primero para aprovechar cache de Docker
COPY pom.xml .

# Copiamos el código fuente del proyecto
COPY src ./src

# Ejecutamos el build del proyecto
# - clean: limpia compilaciones anteriores
# - package: genera el archivo .jar
# -DskipTests: omite tests para acelerar el build en producción
RUN mvn clean package -DskipTests


# ============================================================
# ETAPA 2: IMAGEN FINAL LIGERA PARA PRODUCCIÓN
# ============================================================
# Imagen liviana solo con Java Runtime Environment (no incluye Maven).
FROM eclipse-temurin:21-jre

# Directorio de trabajo dentro del contenedor final
WORKDIR /app

# Copiamos el .jar generado en la etapa builder
# Se toma automáticamente el archivo dentro de /target
COPY --from=builder /app/target/*.jar app.jar


# Puerto que expone el contenedor
# Render mapeará este puerto dinámicamente al externo.
EXPOSE 8080


# Comando final al iniciar el contenedor:
# - Usa el puerto definido por Render mediante la variable PORT
# - Si PORT no existe → usa 8080 por defecto
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]