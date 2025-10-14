package IG.application.Dtos.OrdenTransformacion;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.domain.Clases.DetalleTransformacion;

public record DetalleTransformacionDto(
        Integer id,
        Double cantidad,
        ProductoDto producto,
        UbicacionDto ubicacion,
        Boolean esSalida
) {
    public static DetalleTransformacionDto map(DetalleTransformacion detalle) {
        return new DetalleTransformacionDto(
                null,
                detalle.getCantidad(),
                ProductoDto.map(detalle.getProducto()),
                UbicacionDto.map(detalle.getUbicacion()),
                detalle.isEsSalida()
        );
    }
}