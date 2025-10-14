package IG.application.Dtos.Producto;

import IG.domain.Clases.Producto;
import IG.domain.Enums.ProductoUnidades;

public record ProductoDto(
        Integer id,
        String descripcion,
        double cantidadUnidad,
        ProductoUnidades unidadMedida,
        Double stock,
        TipoProductoDto tipoProducto) {

    public static ProductoDto map(Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getDescripcion(),
                producto.getCantidadUnidad(),
                producto.getUnidadMedida(),
                producto.getStock(),
                TipoProductoDto.map(producto.getTipoProducto())
        );
    }


    public static ProductoDto map(ProductoCache producto) {
         return new ProductoDto(
                 producto.id,
                 producto.descripcion,
                 producto.cantidadUnidad,
                 producto.unidadMedida,
                 producto.stock,
                 producto.tipoProducto
         );
     }

    @Override
    public String toString() {
        return String.format("%s (%.2f %s) - Stock: %.2f - Tipo: %s",
                this.descripcion, this.cantidadUnidad, this.unidadMedida,
                this.stock, this.tipoProducto);
    }
}