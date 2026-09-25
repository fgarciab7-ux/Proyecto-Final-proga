package com.carritogt.dto;

import java.math.BigDecimal;


public class ItemCarritoDTO {

    private Long idShoppingCart;
    private Long idProducto;
    private String nombre;
    private String codigo;
    private String imagen;
    private BigDecimal precio;
    private Integer cantidad;
    private BigDecimal subTotal;

    public ItemCarritoDTO() {
    }

    public ItemCarritoDTO(Long idShoppingCart, Long idProducto, String nombre, String codigo,
                          String imagen, BigDecimal precio, Integer cantidad, BigDecimal subTotal) {
        this.idShoppingCart = idShoppingCart;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.codigo = codigo;
        this.imagen = imagen;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
    }

    public Long getIdShoppingCart() {
        return idShoppingCart;
    }

    public void setIdShoppingCart(Long idShoppingCart) {
        this.idShoppingCart = idShoppingCart;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}
