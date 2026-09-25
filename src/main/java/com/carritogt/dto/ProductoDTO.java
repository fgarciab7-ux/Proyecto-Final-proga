package com.carritogt.dto;

import java.math.BigDecimal;

public class ProductoDTO {

    private Long idProducto;
    private String nombre;
    private String codigo;
    private String imagen;
    private BigDecimal precio;

    public ProductoDTO() {
    }

    public ProductoDTO(Long idProducto, String nombre, String codigo, String imagen, BigDecimal precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.codigo = codigo;
        this.imagen = imagen;
        this.precio = precio;
    }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}