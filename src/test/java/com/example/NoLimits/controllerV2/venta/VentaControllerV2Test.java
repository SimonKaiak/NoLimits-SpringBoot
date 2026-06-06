package com.example.NoLimits.controllerV2.venta;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.venta.VentaModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.venta.VentaControllerV2;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.service.venta.VentaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("VentaControllerV2 Tests")
class VentaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private VentaModelAssembler ventaAssembler;

    private VentaResponseDTO crearVenta() {

        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(1L);
        dto.setUsuarioId(1L);
        dto.setMetodoPagoId(1L);
        dto.setMetodoEnvioId(1L);
        dto.setEstadoId(1L);
        dto.setTotalVenta(25980f);

        return dto;
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("Debe listar ventas")
        void debeListarVentas() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.findAll())
                    .thenReturn(List.of(dto));

            when(ventaAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/ventas"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 si no hay ventas")
        void debeRetornar204SinVentas() throws Exception {

            when(ventaService.findAll())
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v2/ventas"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe buscar venta por id")
        void debeBuscarVentaPorId() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.findById(1L))
                    .thenReturn(dto);

            when(ventaAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/ventas/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 cuando no existe")
        void debeRetornar404() throws Exception {

            when(ventaService.findById(999L))
                    .thenThrow(new RecursoNoEncontradoException("No encontrada"));

            mockMvc.perform(get("/api/v2/ventas/999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe buscar ventas por metodo de pago")
        void debeBuscarPorMetodoPago() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.findByMetodoPago(1L))
                    .thenReturn(List.of(dto));

            when(ventaAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(get("/api/v2/ventas/metodopago/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 204 si no existen ventas por metodo pago")
        void debeRetornar204MetodoPago() throws Exception {

            when(ventaService.findByMetodoPago(1L))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v2/ventas/metodopago/1"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("Debe crear venta")
        void debeCrearVenta() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.crearVentaDesdeRequest(any(), eq(1L)))
                    .thenReturn(dto);

            when(ventaAssembler.toModel(any()))
                    .thenReturn(
                            EntityModel.of(dto)
                                    .add(Link.of(
                                            "http://localhost/api/v2/ventas/1",
                                            "self"))
                    );

            mockMvc.perform(post("/api/v2/ventas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                  "usuarioId":1,
                                  "metodoPagoId":1,
                                  "metodoEnvioId":1,
                                  "estadoId":1,
                                  "detalles":[
                                    {
                                      "productoId":10,
                                      "cantidad":2,
                                      "precioUnitario":12990
                                    }
                                  ]
                                }
                                """))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Debe crear venta con JSON vacío")
        void debeCrearVentaConJsonVacio() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.crearVentaDesdeRequest(any(), any()))
                    .thenReturn(dto);

            when(ventaAssembler.toModel(any()))
                    .thenReturn(
                            EntityModel.of(dto)
                                    .add(Link.of(
                                            "http://localhost/api/v2/ventas/1",
                                            "self"))
                    );

            mockMvc.perform(post("/api/v2/ventas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Actualización")
    class Actualizacion {

        @Test
        @DisplayName("Debe actualizar venta con PUT")
        void debeActualizarConPut() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.update(eq(1L), any()))
                    .thenReturn(dto);

            when(ventaAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(put("/api/v2/ventas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "metodoPagoId":1,
                                        "metodoEnvioId":1,
                                        "estadoId":2
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 en PUT")
        void debeRetornar404Put() throws Exception {

            when(ventaService.update(eq(999L), any()))
                    .thenThrow(new RecursoNoEncontradoException("No encontrada"));

            mockMvc.perform(put("/api/v2/ventas/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "estadoId":2
                                    }
                                    """))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe actualizar venta con PATCH")
        void debeActualizarPatch() throws Exception {

            VentaResponseDTO dto = crearVenta();

            when(ventaService.patch(eq(1L), any()))
                    .thenReturn(dto);

            when(ventaAssembler.toModel(any()))
                    .thenReturn(EntityModel.of(dto));

            mockMvc.perform(patch("/api/v2/ventas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "estadoId":2
                                    }
                                    """))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe retornar 404 en PATCH")
        void debeRetornar404Patch() throws Exception {

            when(ventaService.patch(eq(999L), any()))
                    .thenThrow(new RecursoNoEncontradoException("No encontrada"));

            mockMvc.perform(patch("/api/v2/ventas/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "estadoId":2
                                    }
                                    """))
                    .andExpect(status().isNotFound());
        }
    }


    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("Debe eliminar venta")
        void debeEliminarVenta() throws Exception {

            mockMvc.perform(delete("/api/v2/ventas/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe retornar 404 al eliminar inexistente")
        void debeRetornar404Delete() throws Exception {

            doThrow(new RecursoNoEncontradoException("No encontrada"))
                    .when(ventaService)
                    .deleteById(999L);

            mockMvc.perform(delete("/api/v2/ventas/999"))
                    .andExpect(status().isNotFound());
        }
    }
}