package main.java.IG.domain.Clases;

import main.java.IG.domain.Clases.Producto;

public class DetalleTransformacion {
    private Producto producto;
    private double cantidad;
    private Ubicacion ubicacion;
    private boolean esSalida;

    public DetalleTransformacion(Producto producto, double cantidad, Ubicacion ubicacion, boolean esSalida) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.esSalida = esSalida;
    }

    public DetalleTransformacion() {
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isEsSalida() {
        return esSalida;
    }

    public void setEsSalida(boolean esSalida) {
        this.esSalida = esSalida;
    }

}

