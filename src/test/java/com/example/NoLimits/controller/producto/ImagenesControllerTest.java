package com.example.NoLimits.controller.producto;

import com.example.NoLimits.Multimedia.controller.producto.ImagenesController;
import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;
import com.example.NoLimits.Multimedia.service.producto.ImagenesService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImagenesController.class)
@AutoConfigureMockMvc(addFilters = false)
class ImagenesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImagenesService imagenesService;

    @Nested
    @DisplayName("Listar Imagenes")
    class ListarImagenesTests {

        @Test
        @DisplayName("Debe listar imágenes")
        void debeListarImagenes() throws Exception {

            // Arrange
            when(imagenesService.findAll())
                    .thenReturn(List.of(new ImagenesResponseDTO()));

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen imágenes")
        void debeRetornarNoContentCuandoNoExistenImagenes() throws Exception {

            // Arrange
            when(imagenesService.findAll())
                    .thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Obtener Imagenes")
    class ObtenerImagenesTests {

        @Test
        @DisplayName("Debe obtener imagen por ID")
        void debeObtenerImagenPorId() throws Exception {

            // Arrange
            when(imagenesService.findById(1L))
                    .thenReturn(new ImagenesResponseDTO());

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe obtener imágenes por producto")
        void debeObtenerImagenesPorProducto() throws Exception {

            // Arrange
            when(imagenesService.findByProducto(1L))
                    .thenReturn(List.of(new ImagenesResponseDTO()));

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes/producto/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando el producto no tiene imágenes")
        void debeRetornarNoContentProductoSinImagenes() throws Exception {

            // Arrange
            when(imagenesService.findByProducto(1L))
                    .thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes/producto/1"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Busqueda de Imagenes")
    class BusquedaImagenesTests {

        @Test
        @DisplayName("Debe buscar imágenes por ruta")
        void debeBuscarImagenesPorRuta() throws Exception {

            // Arrange
            when(imagenesService.findByRutaContainingIgnoreCase("spider"))
                    .thenReturn(List.of(new ImagenesResponseDTO()));

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes/buscar")
                    .param("ruta", "spider"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no encuentra imágenes por ruta")
        void debeRetornarNoContentCuandoNoEncuentraImagenesPorRuta() throws Exception {

                // Arrange
                when(imagenesService.findByRutaContainingIgnoreCase("inexistente"))
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/imagenes/buscar")
                                .param("ruta", "inexistente"))
                        .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Resumen de Imagenes")
    class ResumenImagenesTests {

        @Test
        @DisplayName("Debe obtener resumen de imágenes")
        void debeObtenerResumenImagenes() throws Exception {

            // Arrange
            when(imagenesService.obtenerImagenesResumen())
                    .thenReturn(List.of(Map.of("id", 1)));

            // Act & Assert
            mockMvc.perform(get("/api/v1/imagenes/resumen"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 cuando no existen imágenes en el resumen")
        void debeRetornarNoContentCuandoResumenEstaVacio() throws Exception {

                // Arrange
                when(imagenesService.obtenerImagenesResumen())
                        .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/v1/imagenes/resumen"))
                        .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Crear Imagen")
    class CrearImagenTests {

        @Test
        @DisplayName("Debe crear imagen")
        void debeCrearImagen() throws Exception {

            // Arrange
            when(imagenesService.save(any()))
                    .thenReturn(new ImagenesResponseDTO());

            // Act & Assert
            mockMvc.perform(post("/api/v1/imagenes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "ruta": "/assets/img/spiderman.webp",
                                "productoId": 1
                            }
                            """))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Actualizar Imagen")
    class ActualizarImagenTests {

        @Test
        @DisplayName("Debe actualizar imagen")
        void debeActualizarImagen() throws Exception {

            // Arrange
            when(imagenesService.update(eq(1L), any()))
                    .thenReturn(new ImagenesResponseDTO());

            // Act & Assert
            mockMvc.perform(put("/api/v1/imagenes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe editar imagen parcialmente")
        void debeEditarImagenParcialmente() throws Exception {

            // Arrange
            when(imagenesService.patch(eq(1L), any()))
                    .thenReturn(new ImagenesResponseDTO());

            // Act & Assert
            mockMvc.perform(patch("/api/v1/imagenes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Eliminar Imagen")
    class EliminarImagenTests {

        @Test
        @DisplayName("Debe eliminar imagen")
        void debeEliminarImagen() throws Exception {

            // Arrange
            doNothing().when(imagenesService)
                    .deleteById(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/v1/imagenes/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe eliminar imágenes por producto")
        void debeEliminarImagenesPorProducto() throws Exception {

            // Arrange
            when(imagenesService.deleteByProducto(1L))
                    .thenReturn(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/v1/imagenes/producto/1"))
                    .andExpect(status().isNoContent());
        }
    }
}