package IG.domain.Clases;

import IG.domain.Enums.EstadosOrdenes;

public class EnProcesoEstado implements EstadoOrdenMovimiento {
    @Override
    public EstadosOrdenes getEstado() {
        return EstadosOrdenes.PROCESO;
    }

    @Override
    public void aprobar(OrdenMovimiento orden) {
        throw new IllegalStateException("La orden ya está en proceso. No se puede aprobar nuevamente.");
    }

    @Override
    public void ejecutar(OrdenMovimiento orden) {
        orden.setEstadoOrden(new CompletadaEstado());
        orden.setEstado(EstadosOrdenes.COMPLETADO);
    }

    @Override
    public void completar(OrdenMovimiento orden) {
        throw new IllegalStateException("Debe ejecutar la orden antes de completarla.");
    }

    @Override
    public void cancelar(OrdenMovimiento orden) {
        orden.setEstadoOrden(new CanceladaEstado());
        orden.setEstado(EstadosOrdenes.CANCELADO);
    }
}

