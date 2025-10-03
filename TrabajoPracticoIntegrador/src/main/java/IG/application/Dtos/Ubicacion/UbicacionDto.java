package IG.application.Dtos.Ubicacion;

import IG.domain.Clases.Ubicacion;

public record UbicacionDto(
        Integer id,
        Integer nroEstanteria,
        Integer nroNivel,
        Double capacidadUsada,
        ZonaDto zona) {
    public static UbicacionDto map(Ubicacion ubicacion) {
        return new UbicacionDto(
                ubicacion.getId(),
                ubicacion.getNroEstanteria(),
                ubicacion.getNroNivel(),
                ubicacion.getCapacidadUsada(),
                ZonaDto.map(ubicacion.getZona())
        );
    }

    @Override
    public String toString() {
        return String.format("Estanteria: %d, Nivel: %d, Zona: %s", this.nroEstanteria, this.nroNivel, this.zona);
    }
}
