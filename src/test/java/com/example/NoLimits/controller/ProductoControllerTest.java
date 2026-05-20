package com.example.NoLimits.controller;

import com.example.NoLimits.Multimedia.controller.producto.ProductoController;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    @DisplayName("Debe listar productos correctamente")
    void debeListarProductos() throws Exception {

        ProductoResumenDTO producto = new ProductoResumenDTO();

        when(productoService.findAll())
                .thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}