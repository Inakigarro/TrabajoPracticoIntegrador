package IG.domain.Clases;

import IG.domain.Enums.EstadosOrdenes;

public class CanceladaEstado implements EstadoOrdenMovimiento {
    @Override
    public EstadosOrdenes getEstado() {
        return EstadosOrdenes.CANCELADO;
    }

    @Override
    public void aprobar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede aprobar una orden cancelada.");
    }

    @Override
    public void ejecutar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede ejecutar una orden cancelada.");
    }

    @Override
    public void completar(OrdenMovimiento orden) {
        throw new IllegalStateException("No se puede completar una orden cancelada.");
    }

    @Override
    public void cancelar(OrdenMovimiento orden) {
        throw new IllegalStateException("La orden ya está cancelada.");
    }
}

