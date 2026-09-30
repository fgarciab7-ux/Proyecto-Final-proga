package com.carritogt.service.impl;

import com.carritogt.dto.ProductoDTO;
import com.carritogt.model.Producto;
import com.carritogt.repository.ProductoRepository;
import com.carritogt.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::aDTO)
                .collect(Collectors.toList());
    }

    private ProductoDTO aDTO(Producto p) {
        return new ProductoDTO(p.getIdProducto(), p.getNombre(), p.getCodigo(), p.getImagen(), p.getPrecio());
    }
}
