package IG.domain.Clases;

import IG.domain.Enums.EstadosOrdenes;

public class CompletadaEstado implements EstadoOrdenMovimiento {
    @Override
    public EstadosOrdenes getEstado() {
        return EstadosOrdenes.COMPLETADO;
    }

    @Override
    public void aprobar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede aprobar una orden completada.");
    }

    @Override
    public void ejecutar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede ejecutar una orden completada.");
    }

    @Override
    public void completar(OrdenMovimiento orden) {
        throw new IllegalStateException("La orden ya está completada.");
    }

    @Override
    public void cancelar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede cancelar una orden completada.");
    }
}

