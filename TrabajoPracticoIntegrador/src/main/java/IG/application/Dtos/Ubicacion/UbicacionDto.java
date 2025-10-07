package IG.application.Dtos.Ubicacion;

import IG.application.Dtos.ProductoUbicacionDto;
import IG.domain.Clases.Ubicacion;

import java.util.ArrayList;
import java.util.List;

public record UbicacionDto(
        Integer id,
        Integer nroEstanteria,
        Integer nroNivel,
        Double capacidadUsada,
        ZonaDto zona,
        List<ProductoUbicacionDto> productos) {
    public static UbicacionDto map(Ubicacion ubicacion) {
        List<ProductoUbicacionDto> productos = new ArrayList<>();
        var dto = new UbicacionDto(
                ubicacion.getId(),
                ubicacion.getNroEstanteria(),
                ubicacion.getNroNivel(),
                ubicacion.getCapacidadUsada(),
                ZonaDto.map(ubicacion.getZona()),
                productos
        );

        if (ubicacion.getProductos() != null && !ubicacion.getProductos().isEmpty()) {
            productos.addAll(ubicacion.getProductos()
                    .stream().map(ProductoUbicacionDto::map).toList());
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
            List.of() // Lista vacía para evitar recursividad
        );
    }

    @Override
    public String toString() {
        return String.format("Estanteria: %d, Nivel: %d, Zona: %s", this.nroEstanteria, this.nroNivel, this.zona);
    }
}
