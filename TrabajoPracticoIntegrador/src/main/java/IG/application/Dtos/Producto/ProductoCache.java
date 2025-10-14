package IG.application.Dtos.Producto;

import IG.domain.Clases.Producto;
import IG.domain.Enums.ProductoUnidades;

public class ProductoCache {
    public Integer id;
    public String descripcion;
    public double cantidadUnidad;
    public ProductoUnidades unidadMedida;
    public Double stock;
    public TipoProductoDto tipoProducto;

    public ProductoCache(Integer id, String descripcion, double cantidadUnidad,
                         ProductoUnidades unidadMedida, Double stock, TipoProductoDto tipoProducto) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidadUnidad = cantidadUnidad;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
        this.tipoProducto = tipoProducto;
    }

    // ✅ MÉTODO CRÍTICO: Convierte ProductoCache a Double (para los .map() de la view)
    public static Double map(ProductoCache productoCache) {
        return productoCache.stock(); // Retorna el stock como Double
    }

    // ✅ Métodos de conversión adicionales
    public static ProductoCache map(Producto producto) {
        return new ProductoCache(
                producto.getId(),
                producto.getDescripcion(),
                producto.getCantidadUnidad(),
                producto.getUnidadMedida(),
                producto.getStock(),
                TipoProductoDto.map(producto.getTipoProducto())
        );
    }

    public static ProductoCache map(ProductoDto productoDto) {
        return new ProductoCache(
                productoDto.id(),
                productoDto.descripcion(),
                productoDto.cantidadUnidad(),
                productoDto.unidadMedida(),
                productoDto.stock(),
                productoDto.tipoProducto()
        );
    }

    public ProductoDto toProductoDto() {
        return new ProductoDto(
                this.id,
                this.descripcion,
                this.cantidadUnidad,
                this.unidadMedida,
                this.stock,
                this.tipoProducto
        );
    }

    // ✅ Métodos de acceso (que la view espera)
    public Integer id() {
        return this.id;
    }

    public Double stock() {
        return this.stock;
    }

    public String descripcion() {
        return this.descripcion;
    }

    public Double cantidadUnidad() {
        return this.cantidadUnidad;
    }

    public ProductoUnidades unidadMedida() {
        return this.unidadMedida;
    }

    public TipoProductoDto tipoProducto() {
        return this.tipoProducto;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f %s) - Stock: %.2f",
                descripcion, cantidadUnidad, unidadMedida, stock);
    }
}