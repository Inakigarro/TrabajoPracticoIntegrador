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
                ProductoDto.map(prodUbi.getProducto()),
                UbicacionDto.map(prodUbi.getUbicacion()),
                prodUbi.getStockProductoUbicacion()
        );
    }

    public static ProductoUbicacionDto map(ProductoUbicacionCache prodUbi) {
        return new ProductoUbicacionDto(
                prodUbi.id,
                ProductoDto.map(prodUbi.producto),
                UbicacionDto.map(prodUbi.ubicacion),
                prodUbi.stockProductoUbicacion
        );
    }
}
