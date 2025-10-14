package IG.application.interfaces;

import IG.application.Dtos.OrdenTransformacion.OrdenTransformacionDto;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoTransformacion;

import java.util.List;

public interface IServicioOrdenTransformacion {
    OrdenTransformacionDto crearOrden(OrdenTransformacionDto ordenTransformacionDto);
    List<OrdenTransformacionDto> listarOrdenes(int pageSize, int pageNumber);
    OrdenTransformacionDto buscarPorId(int id);
    List<OrdenTransformacionDto> listarPorTipo(TipoTransformacion tipo);
    List<OrdenTransformacionDto> listarPorEstado(EstadosOrdenes estado);
    void modificarEstado(int id);
    void eliminarOrden(int id);
    void cancelarOrden(int id);
}
