package com.carritogt.repository;

import com.carritogt.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CarritoRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByCodUsuario(String codUsuario);

    Optional<ItemCarrito> findByCodUsuarioAndProducto_IdProducto(String codUsuario, Long idProducto);

    void deleteByCodUsuario(String codUsuario);
}
