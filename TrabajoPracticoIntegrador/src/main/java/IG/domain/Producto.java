package IG.domain;

import main.java.IG.domain.Constants.ProductoConstants;

public class Producto {
    private Integer id;
    private String descripcion;
    private String unidadMedida;
    private Double stock;

    public Producto() {
        this.id = 0;
        this.descripcion = "";
        this.unidadMedida = "";
        this.stock = 0.0;
    }

    public Producto(
            Integer id,
            String descripcion,
            String unidadMedida,
            Double stock) {
        this.setId(id);
        this.setDescripcion(descripcion);
        this.unidadMedida = unidadMedida;
        this.setStock(stock);
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

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        if (stock == null || stock <= ProductoConstants.PRODUCTO_STOCK_MIN) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a cero.");
        }

        this.stock = stock;
    }
}
