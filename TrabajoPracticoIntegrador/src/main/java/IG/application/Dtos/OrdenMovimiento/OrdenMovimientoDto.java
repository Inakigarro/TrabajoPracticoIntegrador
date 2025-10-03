package IG.application.Dtos.OrdenMovimiento;

import IG.domain.Clases.OrdenMovimiento;
import IG.domain.Enums.TipoMovimiento;
import IG.domain.Enums.EstadosOrdenes;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenMovimientoDto(
    Integer id,
    TipoMovimiento tipo,
    LocalDateTime fecha,
    EstadosOrdenes estado,
    List<DetalleMovimientoDto> detalleMovimientoList
) {
    public static OrdenMovimientoDto map(OrdenMovimiento ordenMovimiento) {
        return new OrdenMovimientoDto(
            ordenMovimiento.getId(),
            ordenMovimiento.getTipo(),
            ordenMovimiento.getFecha(),
            ordenMovimiento.getEstado(),
            ordenMovimiento.getDetalleMovimientoList().stream()
                .map(DetalleMovimientoDto::map)
                .toList()
        );
    }
}

