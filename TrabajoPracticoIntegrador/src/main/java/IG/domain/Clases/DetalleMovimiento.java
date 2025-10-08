package IG.domain.Clases;


import IG.application.Dtos.OrdenMovimiento.DetalleMovimientoDto;

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

    public void setId(Integer id) {
        if (id != null && id < 0) throw new IllegalArgumentException("El id no puede ser negativo");
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no debe ser nulo");
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }

    public Boolean esSalida() {
        return esSalida;
    }
    public void setEsSalida(Boolean esSalida) {
        if (esSalida == null) throw new IllegalArgumentException("esSalida no puede ser nulo");
        this.esSalida = esSalida;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(Ubicacion ubicacion) {
        if (ubicacion == null) throw new IllegalArgumentException("La ubicación no debe ser nula");
        this.ubicacion = ubicacion;
    }

    public OrdenMovimiento getOrdenMovimiento() {
        return ordenMovimiento;
    }
    public void setOrdenMovimiento(OrdenMovimiento ordenMovimiento) {
        if (ordenMovimiento == null) throw new IllegalArgumentException("La orden de movimiento no debe ser nula");
        this.ordenMovimiento = ordenMovimiento;
    }

    public Double getCantidad() {
        return cantidad;
    }
    public void setCantidad(Double cantidad) {
        if (cantidad == null || cantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser nula ni negativa");
        this.cantidad = cantidad;
    }

    public ProductoUbicacion getProductoUbicacion() {
        if (producto == null || ubicacion == null) return null;
        ProductoUbicacion pu = producto.getProductoUbicacionPorUbicacionId(ubicacion.getId());
        if (pu == null) {
            pu = new ProductoUbicacion(producto, ubicacion, 0.0);
            producto.getUbicaciones().add(pu);
            ubicacion.getProductos().add(pu);
        }
        return pu;
    }

    public static DetalleMovimiento map(DetalleMovimientoDto dto) {
        DetalleMovimiento entity = new DetalleMovimiento();
        entity.setId(dto.id());
        entity.setCantidad(dto.cantidad());
        entity.setEsSalida(dto.esSalida());
        if (dto.producto() != null)
            entity.setProducto(Producto.map(dto.producto()));

        if (dto.ubicacion() != null)
            entity.setUbicacion(Ubicacion.map(dto.ubicacion()));

        return entity;
    }

    @Override
    public String toString() {
        return String.format("DetalleMovimiento{id=%d, cantidad=%.2f, productoId=%d, ordenMovimientoId=%d, ubicacionId=%d, esSalida=%s}",
                id,
                cantidad,
                producto != null ? producto.getId() : null,
                ordenMovimiento != null ? ordenMovimiento.getId() : null,
                ubicacion != null ? ubicacion.getId() : null,
                esSalida);
    }
}