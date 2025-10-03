package IG.application.Dtos.Ubicacion;

import IG.domain.Clases.Zona;

public record ZonaDto(
        Integer id,
        String tipo,
        NaveDto nave) {

    public static ZonaDto map(Zona zona) {
        return new ZonaDto(
                zona.getId(),
                zona.getTipo().getDescripcion(),
                NaveDto.map(zona.getNave())
        );
    }

    @Override
    public String toString() {
        return String.format("Zona %d, tipo: %s", this.id, this.tipo);
    }
}
