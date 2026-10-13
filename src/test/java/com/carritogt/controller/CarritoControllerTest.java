package com.carritogt.controller;

import com.carritogt.dto.AgregarCarritoRequest;
import com.carritogt.dto.ItemCarritoDTO;
import com.carritogt.exception.ResourceNotFoundException;
import com.carritogt.service.CarritoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarritoService carritoService;

    @Test
    void obtenerCarrito_debeRetornar200YElCarritoDelUsuario() throws Exception {
        ItemCarritoDTO item = new ItemCarritoDTO(1L, 1L, "Camara", "CAM01",
                "images/camera.jpg", new BigDecimal("671.21"), 2, new BigDecimal("1342.42"));
        when(carritoService.obtenerCarrito("user-123")).thenReturn(List.of(item));

        mockMvc.perform(get("/api/carrito/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Camara"))
                .andExpect(jsonPath("$[0].subTotal").value(1342.42));
    }

    @Test
    void agregar_debeRetornar201_cuandoElRequestEsValido() throws Exception {
        AgregarCarritoRequest request = new AgregarCarritoRequest();
        request.setIdProducto(1L);
        request.setCantidad(2);

        mockMvc.perform(post("/api/carrito/user-123/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultado").value("OK"));

        verify(carritoService).agregar(eq("user-123"), any(AgregarCarritoRequest.class));
    }

    @Test
    void agregar_debeRetornar400_cuandoLaCantidadEsInvalida() throws Exception {
        AgregarCarritoRequest request = new AgregarCarritoRequest();
        request.setIdProducto(1L);
        request.setCantidad(0);
        mockMvc.perform(post("/api/carrito/user-123/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(carritoService, never()).agregar(any(), any());
    }

    @Test
    void agregar_debeRetornar404_cuandoElProductoNoExiste() throws Exception {
        AgregarCarritoRequest request = new AgregarCarritoRequest();
        request.setIdProducto(999L);
        request.setCantidad(1);

        doThrow(new ResourceNotFoundException("Producto con id 999 no existe"))
                .when(carritoService).agregar(eq("user-123"), any(AgregarCarritoRequest.class));

        mockMvc.perform(post("/api/carrito/user-123/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultado").value("NO"));
    }

    @Test
    void eliminarItem_debeRetornar200() throws Exception {
        mockMvc.perform(delete("/api/carrito/items/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value("OK"));

        verify(carritoService).eliminarItem(5L);
    }

    @Test
    void vaciar_debeRetornar200() throws Exception {
        mockMvc.perform(delete("/api/carrito/user-123"))
                .andExpect(status().isOk());

        verify(carritoService).vaciarCarrito("user-123");
    }
}