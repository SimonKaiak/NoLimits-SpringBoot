-- Este script se ejecuta automáticamente al iniciar la aplicación
-- gracias a spring.sql.init.mode=always en application.properties

-- Habilitar extensión pgvector (requiere que Neon la soporte, lo hace por defecto)
--CREATE EXTENSION IF NOT EXISTS vector;

-- Crear tabla si no existe (seguro ejecutar múltiples veces)
--CREATE TABLE IF NOT EXISTS producto_embeddings (
 --   id             BIGSERIAL PRIMARY KEY,
 --   producto_id    BIGINT,
 --   titulo         TEXT,
 --   contenido      TEXT,
  --  embedding      vector(1536),
  --  fuente         TEXT,
  --  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--);