package IG.application.Dtos.OrdenMovimiento;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.domain.Clases.DetalleMovimiento;

public record DetalleMovimientoDto(
    Integer id,
    Double cantidad,
    ProductoDto producto,
    Boolean esSalida,
    UbicacionDto ubicacion
) {
    public static DetalleMovimientoDto map(DetalleMovimiento detalle) {
        return new DetalleMovimientoDto(
                detalle.getId(),
                detalle.getCantidad(),
                ProductoDto.map(detalle.getProducto()),
                detalle.esSalida(),
                UbicacionDto.map(detalle.getUbicacion())
        );
    }
}
