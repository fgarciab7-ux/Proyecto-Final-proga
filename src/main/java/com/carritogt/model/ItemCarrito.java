package com.carritogt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idShoppingCart")
    private Long idShoppingCart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    private Producto producto;

    @Column(name = "codUsuario", length = 128)
    private String codUsuario;

    @Column(name = "cantidad")
    private Integer cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto, String codUsuario, Integer cantidad) {
        this.producto = producto;
        this.codUsuario = codUsuario;
        this.cantidad = cantidad;
    }

    public Long getIdShoppingCart() {
        return idShoppingCart;
    }

    public void setIdShoppingCart(Long idShoppingCart) {
        this.idShoppingCart = idShoppingCart;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
