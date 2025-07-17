package main.java.IG.domain.Clases;

public class ProductoUbicacion {
    private Producto producto;
    private Ubicacion ubicacion;
    private Double stockProductoUbicacion;

    public ProductoUbicacion() {
        this.producto = null;
        this.ubicacion = null;
        this.stockProductoUbicacion = 0.0;
    }

    public ProductoUbicacion(Producto producto, Ubicacion ubicacion, Double stockProductoUbicacion) {
        this();
        this.setProducto(producto);
        this.setUbicacion(ubicacion);
        this.setStockProductoUbicacion(stockProductoUbicacion);
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no debe ser nulo");
        }
        this.producto = producto;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException("La ubicación no debe ser nula");
        }
        this.ubicacion = ubicacion;
    }

    public Double getStockProductoUbicacion() {
        return stockProductoUbicacion;
    }

    public void setStockProductoUbicacion(Double stockProductoUbicacion) {
        if (stockProductoUbicacion == null || stockProductoUbicacion < 0) {
            throw new IllegalArgumentException("El stock del producto en la ubicación no puede ser nulo ni negativo.");
        }
        this.stockProductoUbicacion = stockProductoUbicacion;
    }
}
