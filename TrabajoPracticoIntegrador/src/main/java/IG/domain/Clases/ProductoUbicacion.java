package IG.domain.Clases;

public class ProductoUbicacion {
    private Producto producto;
    private Ubicacion ubicacion;
    private Double stockProductoUbicacion;
    private int Id;

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

    public double getStock() {
        return stockProductoUbicacion;
    }

    public void setStock(Double stock) {
        setStockProductoUbicacion(stock);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void sumarStock(double cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad a sumar no puede ser negativa.");
        this.stockProductoUbicacion += cantidad;

        if (this.producto != null) {
            double nuevoStock = this.producto.getStock() + cantidad;
            this.producto.setStock(nuevoStock);
        }
    }
    public void restarStock(double cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad a restar no puede ser negativa.");
        if (this.stockProductoUbicacion < cantidad) throw new IllegalStateException("Stock insuficiente en la ubicación.");
        this.stockProductoUbicacion -= cantidad;
    }
}
