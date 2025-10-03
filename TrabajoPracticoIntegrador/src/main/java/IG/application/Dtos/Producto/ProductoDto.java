package IG.application.Dtos.Producto;

import IG.domain.Clases.Producto;

public record ProductoDto(
        Integer id,
        String descripcion,
        double cantidadUnidad,
        String unidadMedida,
        Double stock,
        TipoProductoDto tipoProducto) {
    public static ProductoDto map(Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getDescripcion(),
                producto.getCantidadUnidad(),
                producto.getUnidadMedida(),
                producto.getStock(),
                new TipoProductoDto(
                        producto.getTipoProducto().getId(),
                        producto.getTipoProducto().getDescripcion()
                )
        );
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f %s) - Stock: %.2f - Tipo: %s", this.descripcion, this.cantidadUnidad, this.unidadMedida, this.stock, this.tipoProducto);
    }
}
