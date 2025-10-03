package IG.domain.Clases;

import java.util.ArrayList;
import java.util.List;

import IG.application.Dtos.Producto.TipoProductoDto;
import IG.domain.Clases.Producto;

public class TipoProducto {
    private Integer id;
    private String descripcion;
    private List<Producto> productos;

    public TipoProducto() {
        this.id = 0;
        this.descripcion = "";
        this.productos = new ArrayList<Producto>();
    }

    public TipoProducto(String descripcion) {
        this();
        this.setDescripcion(descripcion);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id < 1) {
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
        if (descripcion.length() > 150) {
            throw new IllegalArgumentException("La descripción no puede tener más de 150 caracteres.");
        }
        this.descripcion = descripcion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void addProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        this.productos.add(producto);
    }

    public void addRangeProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            throw new IllegalArgumentException("La lista de productos no puede ser nula ni vacía.");
        }

        for (Producto producto : productos) {
            this.addProducto(producto);
        }
    }

    public Boolean existe(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        return this.productos.contains(producto);
    }

    public static TipoProducto map(TipoProductoDto dto) {
        var tipoProducto = new TipoProducto();
        tipoProducto.setId(dto.id());
        tipoProducto.setDescripcion(dto.descripcion());
        return tipoProducto;
    }

    @Override
    public String toString() {
        return String.format("Tipo de Producto: %s", this.descripcion);
    }
}
