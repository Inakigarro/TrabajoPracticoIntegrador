package IG.domain.Clases;


public class DetalleMovimiento {
    private Integer id;
    private Double cantidad;
    private Producto producto;
    private Boolean esSalida;
    private Ubicacion ubicacion;
    private OrdenMovimiento ordenMovimiento;

    public DetalleMovimiento() {
        this.id = 0;
        this.cantidad = 0.0;
        this.esSalida = true;
        this.producto = null;
        this.ubicacion = null;
        this.ordenMovimiento = null;
    }

    public DetalleMovimiento(Producto producto, Ubicacion ubicacion, OrdenMovimiento ordenMovimiento, double cantidad, boolean esSalida) {
        this();
        this.setCantidad(cantidad);
        this.setEsSalida(esSalida);
        this.setProducto(producto);
        this.setUbicacion(ubicacion);
        this.setOrdenMovimiento(ordenMovimiento);
    }

    public Integer getId() {
        return id;
    }

    public void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no debe ser nulo");
        }

        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        if (cantidad <= 0.0)
            throw  new IllegalArgumentException("Error. La cantidad debe ser mayor a cero");

        this.cantidad = cantidad;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException("Error. La ubicacion no puede ser nula.");
        }

        this.ubicacion = ubicacion;
    }

    public void setOrdenMovimiento(OrdenMovimiento ordenMovimiento) {
        this.ordenMovimiento = ordenMovimiento;
    }

    public OrdenMovimiento getOrdenMovimiento() {
        return ordenMovimiento;
    }

    public boolean esSalida() {
        return esSalida;
    }

    public void setEsSalida(boolean esSalida) {
        this.esSalida = esSalida;
    }

    @Override
    public String toString() {
        return String.format("%1$d - %2$s - %3$.2f - %4$b", this.id, this.producto, this.cantidad, this.esSalida);
    }
}