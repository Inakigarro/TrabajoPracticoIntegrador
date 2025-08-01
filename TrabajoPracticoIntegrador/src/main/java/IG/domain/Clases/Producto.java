package main.java.IG.domain.Clases;

import main.java.IG.domain.Constants.ProductoConstants;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private Integer id;
    private String descripcion;
    private double cantidadUnidad;
    private String unidadMedida;
    private Double stock;
    private TipoProducto tipoProducto;
    private List<ProductoUbicacion> ubicaciones;

    public Producto() {
        this.id = 0;
        this.descripcion = "";
        this.cantidadUnidad = 0.0;
        this.unidadMedida = "";
        this.stock = 0.0;
        this.tipoProducto = null;
        this.ubicaciones = new ArrayList<ProductoUbicacion>();
    }

    public Producto(
            Integer id,
            String descripcion,
            Double cantidadUnidad,
            String unidadMedida,
            Double stock,
            TipoProducto tipoProducto) {
        this();
        this.setId(id);
        this.setDescripcion(descripcion);
        this.setCantidadUnidad(cantidadUnidad);
        this.unidadMedida = unidadMedida;
        this.setStock(stock);
        this.setTipoProducto(tipoProducto);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id < ProductoConstants.PRODUCTO_ID_MIN) {
            throw new IllegalArgumentException("El id no puede ser nulo y debe ser mayor a cero.");
        }
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

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
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

    @Override
    public String toString() {
        return String.format("%1$s - %2$.2f %3$s - %4$s - Stock: %5$.2f",
                this.descripcion, this.cantidadUnidad, this.unidadMedida,
                this.tipoProducto, this.stock);
    }
}
