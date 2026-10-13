package com.carritogt.service.impl;


import com.carritogt.dto.AgregarCarritoRequest;
import com.carritogt.dto.ItemCarritoDTO;
import com.carritogt.exception.ResourceNotFoundException;
import com.carritogt.model.ItemCarrito;
import com.carritogt.model.Producto;
import com.carritogt.repository.CarritoRepository;
import com.carritogt.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Producto crearProducto() {
        return new Producto(1L, "Camara", "CAM01", "images/camera.jpg", new BigDecimal("671.21"));
    }

    @Test
    void agregar_debeSumarCantidad_cuandoElProductoYaEstaEnElCarrito() {
        // ARRANGE
        Producto producto = crearProducto();
        String codUsuario = "user-123";
        AgregarCarritoRequest request = new AgregarCarritoRequest();
        request.setIdProducto(1L);
        request.setCantidad(2);

        ItemCarrito itemExistente = new ItemCarrito(producto, codUsuario, 3); // ya tenia 3

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(carritoRepository.findByCodUsuarioAndProducto_IdProducto(codUsuario, 1L))
                .thenReturn(Optional.of(itemExistente));

        carritoService.agregar(codUsuario, request);

        ArgumentCaptor<ItemCarrito> captor = ArgumentCaptor.forClass(ItemCarrito.class);
        verify(carritoRepository).save(captor.capture());
        assertEquals(5, captor.getValue().getCantidad());
    }

    @Test
    void agregar_debeCrearItemNuevo_cuandoElProductoNoEstaEnElCarrito() {
        Producto producto = crearProducto();
        String codUsuario = "user-123";
        AgregarCarritoRequest request = new AgregarCarritoRequest();
        request.setIdProducto(1L);
        request.setCantidad(2);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(carritoRepository.findByCodUsuarioAndProducto_IdProducto(codUsuario, 1L))
                .thenReturn(Optional.empty());

        carritoService.agregar(codUsuario, request);

        ArgumentCaptor<ItemCarrito> captor = ArgumentCaptor.forClass(ItemCarrito.class);
        verify(carritoRepository).save(captor.capture());
        assertEquals(2, captor.getValue().getCantidad());
        assertEquals(codUsuario, captor.getValue().getCodUsuario());
    }

    @Test
    void agregar_debeLanzarExcepcion_cuandoElProductoNoExiste() {
        AgregarCarritoRequest request = new AgregarCarritoRequest();
        request.setIdProducto(999L);
        request.setCantidad(1);

        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                carritoService.agregar("user-123", request));

        verify(carritoRepository, never()).save(any());
    }

    @Test
    void obtenerCarrito_debeCalcularSubTotalCorrectamente() {
        Producto producto = crearProducto(); // precio 671.21
        ItemCarrito item = new ItemCarrito(producto, "user-123", 3);

        when(carritoRepository.findByCodUsuario("user-123")).thenReturn(List.of(item));

        List<ItemCarritoDTO> resultado = carritoService.obtenerCarrito("user-123");

        assertEquals(1, resultado.size());
        assertEquals(new BigDecimal("2013.63"), resultado.get(0).getSubTotal()); // 671.21 * 3
    }

    @Test
    void eliminarItem_debeLanzarExcepcion_cuandoElItemNoExiste() {
        when(carritoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                carritoService.eliminarItem(99L));

        verify(carritoRepository, never()).deleteById(any());
    }

    @Test
    void vaciarCarrito_debeLlamarDeleteByCodUsuario() {
        carritoService.vaciarCarrito("user-123");

        verify(carritoRepository).deleteByCodUsuario("user-123");
    }
}