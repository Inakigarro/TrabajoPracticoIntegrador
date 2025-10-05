package IG.domain.Clases;

import IG.application.Dtos.Producto.ProductoDto;
import IG.domain.Constants.ProductoConstants;
import IG.domain.Enums.ProductoUnidades;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private Integer id;
    private String descripcion;
    private double cantidadUnidad;
    private ProductoUnidades unidadMedida;
    private Double stock;
    private TipoProducto tipoProducto;
    private List<ProductoUbicacion> ubicaciones;

    public Producto() {
        this.id = 0;
        this.descripcion = "";
        this.cantidadUnidad = 0.0;
        this.unidadMedida = ProductoUnidades.SIN_DEFINIR;
        this.stock = 0.0;
        this.tipoProducto = null;
        this.ubicaciones = new ArrayList<ProductoUbicacion>();
    }

    public Producto(
            String descripcion,
            Double cantidadUnidad,
            ProductoUnidades unidadMedida,
            Double stock,
            TipoProducto tipoProducto) {
        this();
        this.setDescripcion(descripcion);
        this.setCantidadUnidad(cantidadUnidad);
        this.setUnidadMedida(unidadMedida);
        this.setStock(stock);
        this.setTipoProducto(tipoProducto);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni vacía.");
        }
        if (descripcion.length() > ProductoConstants.PRODUCTO_DESCRIPCION_MAX_LENGTH) {
            throw new IllegalArgumentException("La descripción no puede tener más de 150 caracteres.");
        }
        this.descripcion = descripcion;
    }

    public ProductoUnidades getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(ProductoUnidades unidadMedida) {
        if (unidadMedida == null || unidadMedida == ProductoUnidades.SIN_DEFINIR) {
            throw new IllegalArgumentException("La unidad de medida no puede ser nula o sin definir.");
        }
        this.unidadMedida = unidadMedida;
    }

    public double getCantidadUnidad() {
        return cantidadUnidad;
    }

    public void setCantidadUnidad(double cantidadUnidad) {
        if (cantidadUnidad <= 0.0) {
            throw new IllegalArgumentException("La cantidad por unidad debe ser mayor a cero.");
        }
        this.cantidadUnidad = cantidadUnidad;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        if (stock == null || stock < ProductoConstants.PRODUCTO_STOCK_MIN) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a cero.");
        }

        this.stock = stock;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        if (tipoProducto == null) {
            throw new IllegalArgumentException("El tipo de producto no puede ser nulo.");
        }

        this.tipoProducto = tipoProducto;

        if (!this.tipoProducto.existe(this)){
            this.tipoProducto.addProducto(this);
        }
    }

    public ProductoUbicacion getProductoUbicacionPorUbicacionId(int ubicacionId) {
        for (ProductoUbicacion pu : ubicaciones) {
            if (pu.getUbicacion() != null && pu.getUbicacion().getId() == ubicacionId) {
                return pu;
            }
        }
        return null;
    }

    public List<ProductoUbicacion> getUbicaciones() {
        return ubicaciones;
    }

    public static Producto map(ProductoDto dto) {
        var prod = new Producto();
        prod.setId(dto.id());
        prod.setDescripcion(dto.descripcion());
        prod.setCantidadUnidad(dto.cantidadUnidad());
        prod.setUnidadMedida(dto.unidadMedida());
        prod.setStock(dto.stock());
        prod.setTipoProducto(TipoProducto.map(dto.tipoProducto()));
        return prod;
    }

    @Override
    public String toString() {
        return String.format("%1$s - %2$.2f %3$s - %4$s - Stock: %5$.2f",
                this.descripcion, this.cantidadUnidad, this.unidadMedida,
                this.tipoProducto, this.stock);
    }
}
