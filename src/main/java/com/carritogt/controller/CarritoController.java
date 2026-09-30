package com.carritogt.controller;

import com.carritogt.dto.AgregarCarritoRequest;
import com.carritogt.dto.ItemCarritoDTO;
import com.carritogt.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    @Autowired
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    //obtiene el carrito de ese usuario
    @GetMapping("/{codUsuario}")
    public ResponseEntity<List<ItemCarritoDTO>> obtenerCarrito(@PathVariable String codUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(codUsuario));
    }

    //  agrega un producto al carrito
    @PostMapping("/{codUsuario}/items")
    public ResponseEntity<Map<String, String>> agregar(@PathVariable String codUsuario,
                                                         @Valid @RequestBody AgregarCarritoRequest request) {
        carritoService.agregar(codUsuario, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("resultado", "OK", "mensaje", "Producto agregado"));
    }

    //  elimina un item especifico del carrito
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Map<String, String>> eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.ok(Map.of("resultado", "OK", "mensaje", "Item eliminado"));
    }

    //  vacia el carrito completo
    @DeleteMapping("/{codUsuario}")
    public ResponseEntity<Map<String, String>> vaciar(@PathVariable String codUsuario) {
        carritoService.vaciarCarrito(codUsuario);
        return ResponseEntity.ok(Map.of("resultado", "OK", "mensaje", "Carrito de compra vaciado"));
    }
}
