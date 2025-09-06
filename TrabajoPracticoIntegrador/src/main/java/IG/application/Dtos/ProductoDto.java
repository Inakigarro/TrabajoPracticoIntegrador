package IG.application.Dtos;

public record ProductoDto(
        Integer id,
        String descripcion,
        double cantidadUnidad,
        String unidadMedida,
        Double stock,
        TipoProductoDto tipoProducto) {
}
