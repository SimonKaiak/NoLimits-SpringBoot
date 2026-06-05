package com.example.NoLimits.controllerV2.producto;


import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.producto.ImagenesModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.producto.ImagenesControllerV2;
import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;
import com.example.NoLimits.Multimedia.service.producto.ImagenesService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImagenesControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)

public class ImagenesControllerV2Test {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImagenesService imagenesService;

    @MockBean
    private ImagenesModelAssembler imagenesAssembler;

    @Test
    @DisplayName("Debe listar imagenes")
    void debeListarImagenes() throws Exception {

        ImagenesResponseDTO dto = new ImagenesResponseDTO();

        when(imagenesService.findAll())
                .thenReturn(List.of(dto));

        when(imagenesAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/imagenes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen imagenes")
    void debeRetornarNoContentCuandoNoHayImagenes() throws Exception {

        when(imagenesService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/imagenes"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar imagen por ID")
    void debeBuscarImagenPorId() throws Exception {

        // Arrange
        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);

        when(imagenesService.findById(1L))
                .thenReturn(dto);

        when(imagenesAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/v2/imagenes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 cuando imagen no existe")
    void debeRetornar404ImagenNoExiste() throws Exception {

        // Arrange
        when(imagenesService.findById(999L))
                .thenThrow(new RecursoNoEncontradoException("Imagen no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/v2/imagenes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe buscar imagenes por producto")
    void debeBuscarImagenesPorProducto() throws Exception {

        // Arrange
        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);

        when(imagenesService.findByProducto(1L))
                .thenReturn(List.of(dto));

        when(imagenesAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/v2/imagenes/producto/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando producto no tiene imagenes")
    void debeRetornarNoContentImagenesPorProducto() throws Exception {

        // Arrange
        when(imagenesService.findByProducto(1L))
                .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/v2/imagenes/producto/1"))
             .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe crear imagen")
    void debeCrearImagen() throws Exception {

        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);

        when(imagenesService.save(any()))
                .thenReturn(dto);

        when(imagenesAssembler.toModel(any()))
                .thenReturn(
                    EntityModel.of(dto)
                        .add(Link.of( "http://localhost/api/v2/imagenes/1", "self"))
                );

        mockMvc.perform(post("/api/v2/imagenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "ruta":"/assets/img/spiderman.webp",
                            "altText":"Spider-Man",
                            "productoId":1
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar imagen con PUT")
    void debeActualizarImagenPut() throws Exception {

        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);

        when(imagenesService.update(eq(1L), any()))
                .thenReturn(dto);

        when(imagenesAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(put("/api/v2/imagenes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "ruta":"/assets/img/nueva.webp",
                            "altText":"Nueva imagen",
                            "productoId":1
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar imagen con PUT")
    void debeRetornar404Update() throws Exception {

        when(imagenesService.update(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("Imagen no encontrada"));

        mockMvc.perform(put("/api/v2/imagenes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "ruta":"test.webp"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe actualizar imagen con PATCH")
    void debeActualizarImagenPatch() throws Exception {

        ImagenesResponseDTO dto = new ImagenesResponseDTO();
        dto.setId(1L);

        when(imagenesService.patch(eq(1L), any()))
                .thenReturn(dto);

        when(imagenesAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(patch("/api/v2/imagenes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "ruta":"/assets/img/patch.webp"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar imagen con PATCH")
    void debeRetornar404Patch() throws Exception {

        when(imagenesService.patch(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("Imagen no encontrada"));

        mockMvc.perform(patch("/api/v2/imagenes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "ruta":"test.webp"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe eliminar imagen")
    void debeEliminarImagen() throws Exception {

        doNothing().when(imagenesService)
                .deleteById(1L);

        mockMvc.perform(delete("/api/v2/imagenes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar imagen")
    void debeRetornar404Delete() throws Exception {

        doThrow(new RecursoNoEncontradoException("Imagen no encontrada"))
                .when(imagenesService)
                .deleteById(999L);

        mockMvc.perform(delete("/api/v2/imagenes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe eliminar imagenes por producto")
    void debeEliminarImagenesPorProducto() throws Exception {

         when(imagenesService.deleteByProducto(1L))
                .thenReturn(1L);

        mockMvc.perform(delete("/api/v2/imagenes/producto/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar imagenes por producto")
    void debeRetornar404DeleteByProducto() throws Exception {

        doThrow(new RecursoNoEncontradoException("Producto no encontrado"))
                .when(imagenesService)
                .deleteByProducto(999L);

        mockMvc.perform(delete("/api/v2/imagenes/producto/999"))
                .andExpect(status().isNotFound());
    }
}
