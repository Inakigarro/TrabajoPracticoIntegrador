package IG.domain.Clases;

import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.application.ServicioUbicaciones;
import IG.application.interfaces.IServicioUbicaciones;
import IG.domain.Enums.EstadosOrdenes;

public class PendienteEstado implements EstadoOrdenMovimiento {
    @Override
    public EstadosOrdenes getEstado() {
        return EstadosOrdenes.PENDIENTE;
    }

    @Override
    public void aprobar(OrdenMovimiento orden) {
        orden.setEstadoOrden(new EnProcesoEstado());
        orden.setEstado(EstadosOrdenes.PROCESO);
    }

    @Override
    public void ejecutar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede ejecutar una orden pendiente. Debe aprobarse primero.");
    }

    @Override
    public void completar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede completar una orden pendiente. Debe aprobarse y ejecutarse primero.");
    }

    @Override
    public void cancelar(OrdenMovimiento orden) {
        orden.setEstadoOrden(new CanceladaEstado());
        orden.setEstado(EstadosOrdenes.CANCELADO);
    }
}