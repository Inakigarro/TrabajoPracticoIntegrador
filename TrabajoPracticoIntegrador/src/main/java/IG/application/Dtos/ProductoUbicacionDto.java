package IG.application.Dtos;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.domain.Clases.ProductoUbicacion;

public record ProductoUbicacionDto(
        Integer id,
        ProductoDto producto,
        UbicacionDto ubicacion,
        Double stockProductoUbicacion
) {
    public static ProductoUbicacionDto map(ProductoUbicacion prodUbi) {
        return new ProductoUbicacionDto(
                prodUbi.getId(),
                prodUbi.getProducto() != null ? ProductoDto.map(prodUbi.getProducto()) : null,
                prodUbi.getUbicacion() != null ? UbicacionDto.map(prodUbi.getUbicacion()) : null,
                prodUbi.getStockProductoUbicacion()
        );
    }

    public static ProductoUbicacionDto map(ProductoUbicacionCache prodUbi) {
        return new ProductoUbicacionDto(
                prodUbi.id,
                prodUbi.producto != null ? ProductoDto.map(prodUbi.producto) : null,
                prodUbi.ubicacion != null ?  UbicacionDto.map(prodUbi.ubicacion) : null,
                prodUbi.stockProductoUbicacion
        );
    }
}
