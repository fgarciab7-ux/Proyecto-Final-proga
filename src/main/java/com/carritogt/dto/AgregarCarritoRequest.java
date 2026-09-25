package com.carritogt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class AgregarCarritoRequest {

    @NotNull(message = "idProducto es obligatorio")
    private Long idProducto;

    @NotNull(message = "cantidad es obligatoria")
    @Min(value = 1, message = "cantidad debe ser al menos 1")
    private Integer cantidad;

    public AgregarCarritoRequest() {
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}