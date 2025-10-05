package IG.application.Dtos.Ubicacion;

import IG.domain.Clases.Nave;

public record NaveDto(Integer id) {
    public static NaveDto map(Nave nave) {
        return new NaveDto(
                nave.getId()
        );
    }

    @Override
    public String toString() {
        return "Nave " + id;
    }
}
