package com.carritogt.controller;

import com.carritogt.dto.ProductoDTO;
import com.carritogt.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    void listar_debeRetornar200YListaDeProductos() throws Exception {
        ProductoDTO producto = new ProductoDTO(1L, "Camara", "CAM01", "images/camera.jpg", new BigDecimal("671.21"));
        when(productoService.obtenerTodos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Camara"))
                .andExpect(jsonPath("$[0].precio").value(671.21));
    }

    @Test
    void listar_debeRetornar200YListaVacia_cuandoNoHayProductos() throws Exception {
        when(productoService.obtenerTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}