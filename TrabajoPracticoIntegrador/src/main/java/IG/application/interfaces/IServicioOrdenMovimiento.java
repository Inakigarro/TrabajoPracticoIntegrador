package IG.application.interfaces;

import IG.application.Dtos.OrdenMovimiento.OrdenMovimientoDto;
import IG.application.Dtos.OrdenMovimiento.DetalleMovimientoDto;
import IG.domain.Enums.TipoMovimiento;
import IG.domain.Enums.EstadosOrdenes;

import java.sql.SQLException;
import java.util.List;

public interface IServicioOrdenMovimiento {
    OrdenMovimientoDto crearOrden(OrdenMovimientoDto ordenMovimientoDto);
    List<OrdenMovimientoDto> listarOrdenes(int pageSize, int pageNumber);
    OrdenMovimientoDto buscarPorId(int id);
    List<OrdenMovimientoDto> listarPorTipo(TipoMovimiento tipo);
    List<OrdenMovimientoDto> listarPorEstado(EstadosOrdenes estado);
    void modificarEstado(int id, EstadosOrdenes nuevoEstado);
    void actualizarOrden(OrdenMovimientoDto ordenMovimientoDto);
    void eliminarOrden(int id);
    List<DetalleMovimientoDto> listarDetallesPorOrden(int ordenId);
    void actualizarDetallesOrdenMovimiento(Integer orderId, List<DetalleMovimientoDto> detalles) throws IllegalArgumentException, SQLException;
}
