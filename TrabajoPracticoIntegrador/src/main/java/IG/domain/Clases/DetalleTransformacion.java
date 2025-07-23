package main.java.IG.domain.Clases;

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
        this.producto = null;
        this.cantidad = 0.0;
        this.ubicacion = null;
        this.esSalida = true;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        if (producto==null)
            throw new IllegalArgumentException("El producto no puede ser nulo");

        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        if (cantidad == 0.0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");

        this.cantidad = cantidad;
    }

    public main.java.IG.domain.Clases.Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(main.java.IG.domain.Clases.Ubicacion ubicacion) {
        if (ubicacion==null)
            throw new IllegalArgumentException("La ubicacion no debe ser nula");

        this.ubicacion = ubicacion;
    }

    public boolean isEsSalida() {
        return esSalida;
    }

    public void setEsSalida(boolean esSalida) {
        this.esSalida = esSalida;
    }

}

