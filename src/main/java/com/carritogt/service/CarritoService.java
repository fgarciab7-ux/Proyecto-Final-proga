package com.carritogt.service;

import com.carritogt.dto.AgregarCarritoRequest;
import com.carritogt.dto.ItemCarritoDTO;

import java.util.List;

public interface CarritoService {

    List<ItemCarritoDTO> obtenerCarrito(String codUsuario);

    void agregar(String codUsuario, AgregarCarritoRequest request);

    void eliminarItem(Long idShoppingCart);

    void vaciarCarrito(String codUsuario);
}
