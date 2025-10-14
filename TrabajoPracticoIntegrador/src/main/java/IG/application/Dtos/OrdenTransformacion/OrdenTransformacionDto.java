package IG.application.Dtos.OrdenTransformacion;

import IG.domain.Clases.OrdenTransformacion;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoTransformacion;

import java.time.LocalDateTime;
import java.util.List;

public record OrdenTransformacionDto(
        Integer id,
        TipoTransformacion tipo,
        LocalDateTime fecha,
        EstadosOrdenes estado,
        List<DetalleTransformacionDto> detalleTransformacionList
) {
    public static OrdenTransformacionDto map(OrdenTransformacion ordenTransformacion) {
        return new OrdenTransformacionDto(
                ordenTransformacion.getId(),
                ordenTransformacion.getTipo(),
                ordenTransformacion.getFecha(),
                ordenTransformacion.getEstado(),
                ordenTransformacion.getDetalleTransformacionList().stream()
                        .map(DetalleTransformacionDto::map)
                        .toList()
        );
    }
}
