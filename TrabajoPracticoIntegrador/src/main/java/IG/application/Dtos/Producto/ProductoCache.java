package IG.application.Dtos.Producto;

import IG.domain.Enums.ProductoUnidades;

public class ProductoCache {
    public Integer id;
    public String descripcion;
    public double cantidadUnidad;
    public ProductoUnidades unidadMedida;
    public Double stock;
    public TipoProductoDto tipoProducto;

    public ProductoCache(Integer id, String descripcion, double cantidadUnidad, ProductoUnidades unidadMedida, Double stock, TipoProductoDto tipoProducto) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidadUnidad = cantidadUnidad;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
        this.tipoProducto = tipoProducto;
    }

    public static ProductoCache map(ProductoDto producto) {
        return new ProductoCache(
            producto.id(),
            producto.descripcion(),
            producto.cantidadUnidad(),
            producto.unidadMedida(),
            producto.stock(),
            producto.tipoProducto()
        );
    }
}
