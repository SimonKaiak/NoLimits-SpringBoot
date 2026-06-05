package com.example.NoLimits.controllerV2.producto;

import com.example.NoLimits.Multimedia.controllerV2.producto.ProductoControllerV2;
import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.producto.ProductoModelAssembler;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ProductoModelAssembler productoAssembler;

    @Test
    @DisplayName("Debe listar productos")
    void debeListarProductos() throws Exception {

        ProductoResumenDTO dto =
                new ProductoResumenDTO(
                        1L,
                        "Spider-Man",
                        19990.0,
                        "PELICULA",
                        "ACTIVO",
                        null,
                        null,
                        null
                );

        when(productoService.findAll())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v2/productos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen productos")
    void debeRetornarNoContent() throws Exception {

        when(productoService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/productos"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar producto por ID")
    void debeBuscarProductoPorId() throws Exception {

        //Arrange
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.findById(1L))
                .thenReturn(dto);
        
        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));
        
        //Act & Assert
        mockMvc.perform(get("/api/v2/productos/1"))
                .andExpect(status().isOk());
    
    }

    @Test
    @DisplayName("Debe retornar 404 cuando producto no existe")
    void debeRetornar404ProductoNoExiste() throws Exception {

        //Arrange
        when(productoService.findById(999L))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado"));

                //Act & Assert
        mockMvc.perform(get("/api/v2/productos/999"))
                .andExpect(status().isNotFound());
   
    }

    @Test
    @DisplayName("Debe crear producto")
    void debeCrearProducto() throws Exception {
        // Arrange
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.save(any()))
                .thenReturn(dto);

        EntityModel<ProductoResponseDTO> model =
                EntityModel.of(dto);

        model.add(
                Link.of("http://localhost/api/v2/productos/1")
                        .withSelfRel()
        );

        when(productoAssembler.toModel(any()))
                .thenReturn(model);

        // Act & Assert
        mockMvc.perform(post("/api/v2/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                                "nombre":"Spider-Man",
                                "precio":19990,
                                "tipoProductoId":1,
                                "clasificacionId":1,
                                "estadoId":1
                        }
                        """))
                .andExpect(status().isCreated());
   
    }

    @Test
    @DisplayName("Debe actualizar producto con PUT")
    void debeActualizarProductoConPut() throws Exception {
        //Arrange
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.update(eq(1L), any()))
                .thenReturn(dto);

        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));
        
        //act & Assert
        mockMvc.perform(put("/api/v2/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                                "nombre":"Spider-Man Remasterizado",
                                "precio":24990,
                                "estadoId":1
                        }
                        """))
                .andExpect(status().isOk());
 
    }

    @Test
    @DisplayName("Debe actualizar producto con PATCH")
    void debeActualizarProductoConPatch() throws Exception {
        //Arrange
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.patch(eq(1L), any()))
                .thenReturn(dto);

        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));
        
        //Act & Assert
        mockMvc.perform(patch("/api/v2/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                                "precio":17990
                        }
                        """))
                .andExpect(status().isOk());
                
    }

    @Test
    @DisplayName("Debe eliminar producto")
    void debeEliminarProducto() throws Exception {
        
        // Arrange
        doNothing().when(productoService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/productos/1"))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Debe buscar productos por nombre")
    void debeBuscarProductosPorNombre() throws Exception {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.findByNombre("Spider-Man"))
                .thenReturn(List.of(dto));

        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/productos/nombre/Spider-Man"))
                .andExpect(status().isOk());
    
    
    }

    @Test
    @DisplayName("Debe buscar productos por nombre parcial")
    void debeBuscarProductosPorNombreParcial() throws Exception {

        ProductoResumenDTO dto = new ProductoResumenDTO();
        dto.setId(1L);

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(dto), 1, 1, 1);

        when(productoService.findByNombreContainingIgnoreCase("Spider", 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/nombre/contiene/Spider"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 por nombre parcial vacío")
    void debeRetornar204PorNombreParcial() throws Exception {

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(), 1, 0, 0);

        when(productoService.findByNombreContainingIgnoreCase("Nada", 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/nombre/contiene/Nada"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar productos por clasificación")
    void debeBuscarProductosPorClasificacion() throws Exception {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.findByClasificacion(1L))
                .thenReturn(List.of(dto));

        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/productos/clasificacion/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar productos por tipo")
    void debeBuscarProductosPorTipo() throws Exception {

        ProductoResumenDTO dto = new ProductoResumenDTO();
        dto.setId(1L);

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(dto), 1, 1, 1);

        when(productoService.findByTipoProducto(1L, 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/tipo/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 por tipo vacío")
    void debeRetornar204PorTipo() throws Exception {

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(), 1, 0, 0);

        when(productoService.findByTipoProducto(1L, 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/tipo/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar productos por estado")
    void debeBuscarProductosPorEstado() throws Exception {

        ProductoResumenDTO dto = new ProductoResumenDTO();
        dto.setId(1L);

        PagedResponse<ProductoResumenDTO> respuesta =
                 new PagedResponse<>(List.of(dto), 1, 1, 1);

        when(productoService.findByEstado(1L, 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/estado/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 por estado vacío")
    void debeRetornar204PorEstado() throws Exception {

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(), 1, 0, 0);

        when(productoService.findByEstado(1L, 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/estado/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar productos por tipo y estado")
    void debeBuscarProductosPorTipoYEstado() throws Exception {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.findByTipoProductoAndEstado(1L, 1L))
                .thenReturn(List.of(dto));

        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/productos/tipo/1/estado/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 por tipo y estado vacío")
    void debeRetornar204PorTipoYEstado() throws Exception {

        when(productoService.findByTipoProductoAndEstado(1L, 1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/productos/tipo/1/estado/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar productos por saga")
    void debeBuscarProductosPorSaga() throws Exception {

        ProductoResumenDTO dto = new ProductoResumenDTO();
        dto.setId(1L);

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(dto), 1, 1, 1);

        when(productoService.findBySagaIgnoreCase("Spider-Man", 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/sagas/Spider-Man"))
                .andExpect(status().isOk());
    }  

    @Test
    @DisplayName("Debe retornar 204 por saga vacía")
    void debeRetornar204PorSaga() throws Exception {

        PagedResponse<ProductoResumenDTO> respuesta =
                new PagedResponse<>(List.of(), 1, 0, 0);

        when(productoService.findBySagaIgnoreCase("Nada", 1, 50))
                .thenReturn(respuesta);

        mockMvc.perform(get("/api/v2/productos/sagas/Nada"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe Listar por saga") 
    void debeListarSagas() throws Exception {

        when(productoService.obtenerSagasDistinct())
                .thenReturn(List.of("Spider-Man"));

        mockMvc.perform(get("/api/v2/productos/sagas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe listar sagas por tipo")
    void debeListarSagasPorTipo() throws Exception {

        when(productoService.obtenerSagasDistinctPorTipoProducto(1L))
                .thenReturn(List.of("Spider-Man"));

        mockMvc.perform(get("/api/v2/productos/sagas/tipo/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existan sagas por tipo")
    void debeRetornar204SagasPorTipo() throws Exception {

        when(productoService.obtenerSagasDistinctPorTipoProducto(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/productos/sagas/tipo/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe actualizar precio desde Steam")
    void debeActualizarPrecioDesdeSteam() throws Exception {

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);

        when(productoService.actualizarPrecioDesdeSteam(1L))
                .thenReturn(dto);

        when(productoAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(patch("/api/v2/productos/1/actualizar-precio-steam"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe obtener resumen de productos")
    void debeObtenerResumenProductos() throws Exception {

        when(productoService.obtenerProductosConDatos())
                .thenReturn(List.of(java.util.Map.of("id", 1)));

        mockMvc.perform(get("/api/v2/productos/resumen"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existe resumen")
    void debeRetornarNoContentResumen() throws Exception {

        when(productoService.obtenerProductosConDatos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/productos/resumen"))
                .andExpect(status().isNoContent());
    }


}