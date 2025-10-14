package IG.application.Dtos;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.domain.Clases.Producto;
import IG.domain.Clases.Ubicacion;

public record ProductoUbicacionDto(
        Integer id,
        Double stock,
        ProductoDto producto,
        UbicacionDto ubicacion
) {
    public static ProductoUbicacionDto map(Producto producto, Ubicacion ubicacion, Double stock) {
        return new ProductoUbicacionDto(
                producto.getId(),
                stock,
                ProductoDto.map(producto),
                UbicacionDto.map(ubicacion)
        );
    }

    // ✅ MÉTODO QUE ESPERA LA VIEW - stockProductoUbicacion()
    public Double stockProductoUbicacion() {
        return this.stock;
    }

    // ✅ MÉTODO PARA ProductoUbicacion (entidad)
    public static ProductoUbicacionDto map(IG.domain.Clases.ProductoUbicacion productoUbicacion) {
        return new ProductoUbicacionDto(
                productoUbicacion.getId(),
                productoUbicacion.getStock(),
                ProductoDto.map(productoUbicacion.getProducto()),
                UbicacionDto.map(productoUbicacion.getUbicacion())
        );
    }
}