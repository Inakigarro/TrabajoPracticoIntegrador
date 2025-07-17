package main.java.IG.domain.Clases;


public class DetalleMovimiento {
    private double cantidad;
    private ProductoUbicacion productoUbicacion;
    private boolean esSalida;

    public DetalleMovimiento() {
        this.cantidad = 0.0;
        this.esSalida = true;
        this.productoUbicacion = null;
    }

    public DetalleMovimiento(Producto producto, double cantidad, Ubicacion ubicacion, boolean esSalida) {
        this.setCantidad(cantidad);
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
        if (cantidad == 0.0)
            throw  new IllegalArgumentException("Error. La cantidad debe ser mayor a cero");

        // Verificar que la cantidad sea menor o igual al stock del producto.
        if (cantidad > this.productoUbicacion.getStockProductoUbicacion())
            throw new IllegalArgumentException("La cantidad no puede ser mayor al stock del producto en la ubicación.");

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