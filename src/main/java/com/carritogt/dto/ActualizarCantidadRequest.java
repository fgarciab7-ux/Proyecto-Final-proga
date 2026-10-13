package com.carritogt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ActualizarCantidadRequest {

    @NotNull(message = "cantidad es obligatoria")
    @Min(value = 1, message = "cantidad debe ser al menos 1")
    private Integer cantidad;

    public ActualizarCantidadRequest() {
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
