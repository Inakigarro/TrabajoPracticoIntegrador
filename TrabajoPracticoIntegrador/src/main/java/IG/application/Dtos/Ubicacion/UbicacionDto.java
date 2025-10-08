package IG.application.Dtos.Ubicacion;

import IG.application.Dtos.ProductoUbicacionDto;
import IG.domain.Clases.Ubicacion;

import java.util.HashMap;
import java.util.Map;

public record UbicacionDto(
        Integer id,
        Integer nroEstanteria,
        Integer nroNivel,
        Double capacidadUsada,
        ZonaDto zona,
        Map<Integer, Double> productos) {
    public static UbicacionDto map(Ubicacion ubicacion) {
        Map<Integer, Double> productos = new HashMap<>();
        var dto = new UbicacionDto(
                ubicacion.getId(),
                ubicacion.getNroEstanteria(),
                ubicacion.getNroNivel(),
                ubicacion.getCapacidadUsada(),
                ZonaDto.map(ubicacion.getZona()),
                productos
        );

        if (ubicacion.getProductos() != null && !ubicacion.getProductos().isEmpty()) {
            ubicacion.getProductos().forEach(producto -> {
                productos.put(producto.getProducto().getId(), producto.getStockProductoUbicacion());
            });
        }

        return dto;
    }

    public static UbicacionDto map(UbicacionCache ubicacion) {
        // Para evitar bucle infinito, no mapeamos la lista de productos aquí
        return new UbicacionDto(
            ubicacion.id,
            ubicacion.nroEstanteria,
            ubicacion.nroNivel,
            ubicacion.capacidadUsada,
            ubicacion.zona,
            ubicacion.productos
        );
    }

    @Override
    public String toString() {
        return String.format("Estanteria: %d, Nivel: %d, Zona: %s", this.nroEstanteria, this.nroNivel, this.zona);
    }
}
