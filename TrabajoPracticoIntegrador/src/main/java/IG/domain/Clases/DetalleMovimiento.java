package IG.domain.Clases;


public class DetalleMovimiento {
    private double cantidad;
    private ProductoUbicacion productoUbicacion;
    private boolean esSalida;

    public DetalleMovimiento() {
        this.cantidad = 0.0;
        this.esSalida = true;
        this.productoUbicacion = null;
    }

    public DetalleMovimiento(ProductoUbicacion productoUbicacion, double cantidad, boolean esSalida) {
        this.setProductoUbicacion(productoUbicacion); // primero seteamos la ubicación
        this.setCantidad(cantidad);                   // ahora sí podemos validar contra el stock
        this.setEsSalida(esSalida);
    }

    public void setProductoUbicacion(ProductoUbicacion productoUbicacion) {
        if (productoUbicacion == null) {
            throw new IllegalArgumentException("El producto no debe ser nulo");
        }

        this.productoUbicacion = productoUbicacion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        // solo validamos contra stock si es una salida
        if (esSalida && cantidad > productoUbicacion.getStock()) {
            throw new IllegalArgumentException(
                    "La cantidad no puede ser mayor al stock del producto en la ubicación."
            );
        }
        this.cantidad = cantidad;
    }

    public ProductoUbicacion getProductoUbicacion() {
        return productoUbicacion;
    }

    public void setUbicacion(ProductoUbicacion productoUbicacion) {
        if (productoUbicacion == null) {
            throw new IllegalArgumentException("La ubicacion no debe ser nula");
        }

        this.productoUbicacion = productoUbicacion;
    }

    public boolean esSalida() {
        return esSalida;
    }

    public void setEsSalida(boolean esSalida) {
        this.esSalida = esSalida;
    }
}