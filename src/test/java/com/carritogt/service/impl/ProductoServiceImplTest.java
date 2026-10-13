package com.carritogt.service.impl;

import com.carritogt.dto.ProductoDTO;
import com.carritogt.model.Producto;
import com.carritogt.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void obtenerTodos_debeRetornarListaDeProductoDTO() {

        Producto producto1 = new Producto(1L, "Camara", "CAM01", "images/camera.jpg", new BigDecimal("671.21"));
        Producto producto2 = new Producto(2L, "Laptop", "LAP01", "images/laptop.jpg", new BigDecimal("10232.35"));

        when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2));

        List<ProductoDTO> resultado = productoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Camara", resultado.get(0).getNombre());
        assertEquals("Laptop", resultado.get(1).getNombre());
        assertEquals(new BigDecimal("671.21"), resultado.get(0).getPrecio());

        verify(productoRepository).findAll();
    }

    @Test
    void obtenerTodos_debeRetornarListaVacia_cuandoNoHayProductos() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<ProductoDTO> resultado = productoService.obtenerTodos();

        assertEquals(0, resultado.size());
    }
}