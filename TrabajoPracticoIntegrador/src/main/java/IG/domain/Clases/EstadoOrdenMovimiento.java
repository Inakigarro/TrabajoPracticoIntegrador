package IG.domain.Clases;

import IG.domain.Enums.EstadosOrdenes;

public interface EstadoOrdenMovimiento {
    EstadosOrdenes getEstado();
    void aprobar(OrdenMovimiento orden);
    void ejecutar(OrdenMovimiento orden);
    void completar(OrdenMovimiento orden);
    void cancelar(OrdenMovimiento orden);
}

