package IG.application;

import IG.application.Dtos.OrdenTransformacion.OrdenTransformacionDto;
import IG.application.Dtos.OrdenTransformacion.DetalleTransformacionDto;
import IG.application.interfaces.IServicioOrdenTransformacion;
import IG.config.ConexionBD;
import IG.domain.Clases.OrdenTransformacion;
import IG.domain.Clases.DetalleTransformacion;
import IG.domain.DAO.OrdenTransformacionDAO;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoTransformacion;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ServicioOrdenTransformacion implements IServicioOrdenTransformacion {
    private final Logger logger = Logger.getLogger(ServicioOrdenTransformacion.class.getName());

    public ServicioOrdenTransformacion() {}

    @Override
    public OrdenTransformacionDto crearOrden(OrdenTransformacionDto ordenTransformacionDto) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);

            OrdenTransformacion orden = convertirDtoAEntidad(ordenTransformacionDto);

            logger.info(String.format("Creando Orden de Transformación: %s", orden));
            dao.insertarOrdenTransformacion(orden);

            if (ordenTransformacionDto.detalleTransformacionList() != null &&
                    !ordenTransformacionDto.detalleTransformacionList().isEmpty()) {

                List<DetalleTransformacion> detalles = convertirDetallesDtoAEntidad(
                        ordenTransformacionDto.detalleTransformacionList(), orden);

                for (DetalleTransformacion detalle : detalles) {
                    dao.insertarDetalleTransformacion(detalle, orden.getId());
                }
            }

            logger.info(String.format("Orden de Transformación con Id %d creada exitosamente", orden.getId()));
            return OrdenTransformacionDto.map(orden);
        } catch (SQLException e) {
            logger.severe("Error al crear la Orden de Transformación: " + e.getMessage());
            throw new RuntimeException("Error al crear la orden de transformación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrdenTransformacionDto> listarOrdenes(int pageSize, int pageNumber) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info(String.format("Listando órdenes de transformación (página %d, tamaño %d)", pageNumber, pageSize));

            List<OrdenTransformacion> ordenes = dao.listarTodos();

            int start = (pageNumber - 1) * pageSize;
            int end = Math.min(start + pageSize, ordenes.size());

            return ordenes.subList(start, end).stream()
                    .map(OrdenTransformacionDto::map)
                    .toList();
        } catch (SQLException e) {
            logger.severe("Error al listar órdenes de transformación: " + e.getMessage());
            throw new RuntimeException("Error al listar órdenes de transformación: " + e.getMessage(), e);
        }
    }

    @Override
    public OrdenTransformacionDto buscarPorId(int id) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info("Buscando Orden de Transformación con id: " + id);

            OrdenTransformacion orden = dao.buscarPorId(id);
            return orden == null ? null : OrdenTransformacionDto.map(orden);
        } catch (SQLException e) {
            logger.severe("Error al buscar Orden de Transformación: " + e.getMessage());
            throw new RuntimeException("Error al buscar la orden de transformación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrdenTransformacionDto> listarPorTipo(TipoTransformacion tipo) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info("Listando órdenes de transformación de tipo: " + tipo);

            return dao.listarTodos().stream()
                    .filter(o -> o.getTipo() == tipo)
                    .map(OrdenTransformacionDto::map)
                    .toList();
        } catch (SQLException e) {
            logger.severe("Error al listar órdenes de transformación por tipo: " + e.getMessage());
            throw new RuntimeException("Error al listar órdenes de transformación por tipo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrdenTransformacionDto> listarPorEstado(EstadosOrdenes estado) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info("Listando órdenes de transformación con estado: " + estado);

            return dao.listarTodos().stream()
                    .filter(o -> o.getEstado() == estado)
                    .map(OrdenTransformacionDto::map)
                    .toList();
        } catch (SQLException e) {
            logger.severe("Error al listar órdenes de transformación por estado: " + e.getMessage());
            throw new RuntimeException("Error al listar órdenes de transformación por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificarEstado(int id) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info("Modificando estado de la orden de transformación con id: " + id);

            OrdenTransformacion orden = dao.buscarPorId(id);
            if (orden == null) {
                throw new RuntimeException("Orden no encontrada con id: " + id);
            }

            EstadosOrdenes nuevoEstado;
            switch (orden.getEstado()) {
                case PENDIENTE -> nuevoEstado = EstadosOrdenes.APROBADO;
                case APROBADO -> nuevoEstado = EstadosOrdenes.PROCESO;
                case PROCESO -> nuevoEstado = EstadosOrdenes.COMPLETADO;
                default -> throw new IllegalArgumentException(
                        "No se puede modificar el estado desde: " + orden.getEstado()
                );
            }

            orden.setEstado(nuevoEstado);
            dao.actualizar(orden);
            logger.info("Orden de transformación con Id " + id + " modificada correctamente. Nuevo estado: " + nuevoEstado);
        } catch (SQLException e) {
            logger.severe("Error al modificar estado de la orden de transformación: " + e.getMessage());
            throw new RuntimeException("Error al modificar estado de la orden de transformación: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarOrden(int id) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info("Eliminando Orden de Transformación con id: " + id);

            OrdenTransformacion orden = dao.buscarPorId(id);
            if (orden == null) {
                throw new RuntimeException("Orden no encontrada con id: " + id);
            }

            if (orden.getEstado() == EstadosOrdenes.PROCESO ||
                    orden.getEstado() == EstadosOrdenes.COMPLETADO) {
                throw new IllegalStateException(
                        "No se puede eliminar una orden en estado: " + orden.getEstado()
                );
            }

            dao.eliminar(id);
            logger.info("Orden de transformación eliminada correctamente");
        } catch (SQLException e) {
            logger.severe("Error al eliminar Orden de Transformación: " + e.getMessage());
            throw new RuntimeException("Error al eliminar la orden de transformación: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelarOrden(int id) {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenTransformacionDAO dao = new OrdenTransformacionDAO(conn);
            logger.info("Cancelando Orden de Transformación con id: " + id);

            OrdenTransformacion orden = dao.buscarPorId(id);
            if (orden == null) {
                throw new RuntimeException("Orden no encontrada con id: " + id);
            }

            if (orden.getEstado() == EstadosOrdenes.COMPLETADO ||
                    orden.getEstado() == EstadosOrdenes.CANCELADO) {
                throw new IllegalStateException(
                        "No se puede cancelar una orden en estado: " + orden.getEstado()
                );
            }

            orden.setEstado(EstadosOrdenes.CANCELADO);
            dao.actualizar(orden);
            logger.info("Orden de transformación con Id " + id + " cancelada correctamente");
        } catch (SQLException e) {
            logger.severe("Error al cancelar Orden de Transformación: " + e.getMessage());
            throw new RuntimeException("Error al cancelar la orden de transformación: " + e.getMessage(), e);
        }
    }

    private OrdenTransformacion convertirDtoAEntidad(OrdenTransformacionDto dto) {
        OrdenTransformacion orden = new OrdenTransformacion();
        orden.setId(dto.id());
        orden.setTipo(dto.tipo());
        orden.setFecha(dto.fecha());
        orden.setEstado(dto.estado());
        return orden;
    }

    private List<DetalleTransformacion> convertirDetallesDtoAEntidad(
            List<DetalleTransformacionDto> detallesDto, OrdenTransformacion orden) {

        return detallesDto.stream()
                .map(dto -> {
                    DetalleTransformacion detalle = new DetalleTransformacion();
                    detalle.setCantidad(dto.cantidad());
                    detalle.setEsSalida(dto.esSalida());
                    detalle.setOrdenTransformacion(orden);
                    return detalle;
                })
                .toList();
    }
}