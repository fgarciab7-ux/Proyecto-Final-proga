package com.carritogt.service.impl;

import com.carritogt.dto.AgregarCarritoRequest;
import com.carritogt.dto.ItemCarritoDTO;
import com.carritogt.model.ItemCarrito;
import com.carritogt.model.Producto;
import com.carritogt.repository.CarritoRepository;
import com.carritogt.exception.ResourceNotFoundException;
import com.carritogt.repository.ProductoRepository;
import com.carritogt.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public CarritoServiceImpl(CarritoRepository carritoRepository, ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ItemCarritoDTO> obtenerCarrito(String codUsuario) {
        return carritoRepository.findByCodUsuario(codUsuario)
                .stream()
                .map(this::aDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void agregar(String codUsuario, AgregarCarritoRequest request) {
        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto con id " + request.getIdProducto() + " no existe"));


        carritoRepository.findByCodUsuarioAndProducto_IdProducto(codUsuario, request.getIdProducto())
                .ifPresentOrElse(
                        itemExistente -> {
                            itemExistente.setCantidad(itemExistente.getCantidad() + request.getCantidad());
                            carritoRepository.save(itemExistente);
                        },
                        () -> {
                            ItemCarrito nuevo = new ItemCarrito(producto, codUsuario, request.getCantidad());
                            carritoRepository.save(nuevo);
                        }
                );
    }

    @Override
    @Transactional
    public void eliminarItem(Long idShoppingCart) {
        if (!carritoRepository.existsById(idShoppingCart)) {
            throw new ResourceNotFoundException("Item de carrito con id " + idShoppingCart + " no existe");
        }
        carritoRepository.deleteById(idShoppingCart);
    }

    @Override
    @Transactional
    public void vaciarCarrito(String codUsuario) {
        carritoRepository.deleteByCodUsuario(codUsuario);
    }

    private ItemCarritoDTO aDTO(ItemCarrito item) {
        Producto p = item.getProducto();
        return new ItemCarritoDTO(
                item.getIdShoppingCart(),
                p.getIdProducto(),
                p.getNombre(),
                p.getCodigo(),
                p.getImagen(),
                p.getPrecio(),
                item.getCantidad(),
                p.getPrecio().multiply(java.math.BigDecimal.valueOf(item.getCantidad()))
        );
    }
}
