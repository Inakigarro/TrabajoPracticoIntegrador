package IG.application;

import IG.application.interfaces.IServicioOrdenMovimiento;
import IG.application.Dtos.OrdenMovimiento.OrdenMovimientoDto;
import IG.application.Dtos.OrdenMovimiento.DetalleMovimientoDto;
import IG.config.ConexionBD;
import IG.domain.Clases.*;
import IG.domain.DAO.ProductoUbicacionDAO;
import IG.domain.Enums.TipoMovimiento;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.DAO.OrdenMovimientoDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServicioOrdenMovimiento implements IServicioOrdenMovimiento {
    Logger logger = Logger.getLogger(ServicioOrdenMovimiento.class.getName());

    public ServicioOrdenMovimiento() {}

    public OrdenMovimientoDto crearOrden(OrdenMovimientoDto ordenMovimientoDto) {
        OrdenMovimiento ordenMovimiento = OrdenMovimiento.map(ordenMovimientoDto);
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Creando Orden de Movimiento: %s",  ordenMovimiento));
            ordenMovimientoDAO.insertarOrdenMovimiento(ordenMovimiento);
            if (ordenMovimiento.getDetalleMovimientoList() != null && !ordenMovimiento.getDetalleMovimientoList().isEmpty()) {
                logger.info(String.format("Agregando %d detalles", ordenMovimiento.getDetalleMovimientoList().size()));
                ordenMovimientoDAO.insertarDetalleOrdenMovimiento(ordenMovimiento.getDetalleMovimientoList());
            }
            logger.info(String.format("Orden de Movimiento con Id %d creado exitosamente", ordenMovimiento.getId()));
            return OrdenMovimientoDto.map(ordenMovimiento);
        } catch (SQLException e) {
            logger.severe(String.format("Error al insertar Orden de Movimiento: %s", e.getMessage()));
            throw new RuntimeException("Error al crear la orden de movimiento: " + e.getMessage(), e);
        }
    }

    public List<OrdenMovimientoDto> listarOrdenes(int pageSize, int pageNumber) {
        try(var conn = IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Listando %d detalles", pageSize));
            return ordenMovimientoDAO.listarOrdenMovimiento(pageSize, pageNumber)
                    .stream().map(OrdenMovimientoDto::map).toList();
        } catch (SQLException e) {
            logger.severe(String.format("Error listando %d detalles", pageSize));
            throw new RuntimeException("Error al listar las órdenes de movimiento: " + e.getMessage(), e);
        }
    }

    public OrdenMovimientoDto buscarPorId(int id) {
        try(var conn = IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format(String.format("Buscando Orden de Movimiento: %d", id)));
            OrdenMovimiento orden = ordenMovimientoDAO.buscarOrdenMovimientoPorId(id);
            return orden == null ? null : OrdenMovimientoDto.map(orden);
        } catch (SQLException e) {
            logger.severe(String.format("Error buscando Orden de Movimiento: %d", id));
            throw new RuntimeException("Error al buscar la orden de movimiento por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrdenMovimientoDto> listarPorTipo(TipoMovimiento tipo) {
        try(var conn = IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Listando Ordenes de tipo: %s", tipo));
            return ordenMovimientoDAO.buscarOrdenMovimientoPorTipo(tipo)
                    .stream().map(OrdenMovimientoDto::map).collect(Collectors.toList());
        } catch (SQLException e) {
            logger.severe(String.format("Error listando Ordenes de tipo: %s", tipo));
            throw new RuntimeException("Error al listar órdenes por tipo: " + e.getMessage(), e);
        }
    }

    public List<OrdenMovimientoDto> listarPorEstado(EstadosOrdenes estado) {
        try(var conn = IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Listando Ordenes con estado: %s", estado));
            return ordenMovimientoDAO.listarOrdenMovimiento(100, 1).stream()
                    .filter(o -> o.getEstado() == estado)
                    .map(OrdenMovimientoDto::map)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            logger.severe(String.format("Error listando Ordenes con estado: %s", e.getMessage()));
            throw new RuntimeException("Error al listar órdenes por estado: " + e.getMessage(), e);
        }
    }

    public void modificarEstado(int id) {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Modificando Estado de orden: %d", id));
            OrdenMovimiento orden = ordenMovimientoDAO.buscarOrdenMovimientoPorId(id);
            orden.agregarDetalles(ordenMovimientoDAO.buscarDetallesPorOrdenId(id));
            if (orden == null) throw new RuntimeException("Orden no encontrada");
            switch (orden.getEstado()) {
                case PENDIENTE -> orden.aprobar();
                case PROCESO -> orden.ejecutar();
                default -> throw new IllegalArgumentException("Estado no soportado");
            }
            ordenMovimientoDAO.actualizar(orden);
            logger.info(String.format("Orden de movimiento con Id %d paso de estado correctamente", id));
        } catch (SQLException e) {
            logger.severe(String.format("Error al modificar Estado de orden: %d", id));
            throw new RuntimeException("Error al modificar el estado de la orden: " + e.getMessage(), e);
        }
    }

    public void actualizarOrden(OrdenMovimientoDto ordenMovimientoDto) {
        OrdenMovimiento ordenMovimiento = OrdenMovimiento.map(ordenMovimientoDto);
        try(var conn = IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Actualizando Orden de Movimiento: %s", ordenMovimiento));
            ordenMovimientoDAO.actualizar(ordenMovimiento);
            logger.info("Orden de movimiento actualizada.");
        } catch (SQLException e) {
            logger.severe(String.format("Error al actualizar Orden de Movimiento: %s", e.getMessage()));
            throw new RuntimeException("Error al actualizar la orden de movimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarOrden(int id) {
        try(var conn = IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Eliminando Orden de Movimiento: %d", id));
            ordenMovimientoDAO.eliminar(id);
            logger.info("Orden de movimiento eliminada.");
        } catch (SQLException e) {
            logger.severe(String.format("Error al eliminar Orden de Movimiento: %s", e.getMessage()));
            throw new RuntimeException("Error al eliminar la orden de movimiento: " + e.getMessage(), e);
        }
    }

    public List<DetalleMovimientoDto> listarDetallesPorOrden(int ordenId) {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Listando detalles para orden: %d",  ordenId));
            return ordenMovimientoDAO.buscarDetallesPorOrdenId(ordenId)
                    .stream().map(DetalleMovimientoDto::map).collect(Collectors.toList());
        } catch (SQLException e) {
            logger.severe(String.format("Error listando detalles para orden: %d", ordenId));
            throw new RuntimeException("Error al listar los detalles de la orden de movimiento: " + e.getMessage(), e);
        }
    }

    public void actualizarDetallesOrdenMovimiento(Integer orderId, List<DetalleMovimientoDto> detallesDto) throws IllegalArgumentException, SQLException {
        if (detallesDto == null || detallesDto.isEmpty()) return;

        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenDao = new OrdenMovimientoDAO(conn);
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            logger.info(String.format("Buscando orden de movimiento con id: %d", orderId));
            // Busco la orden de movimiento a modificar.
            OrdenMovimiento orden = ordenDao.buscarOrdenMovimientoPorId(orderId);
            if (orden == null) throw new IllegalArgumentException("No se encontró la orden con Id: " + orderId);

            // Busco si tiene detalles existentes.
            List<DetalleMovimiento> detallesExistentes = ordenDao.buscarDetallesPorOrdenId(orderId);

            var detalles = detallesDto.stream().map(DetalleMovimiento::map).toList();

            // Busco los detalles nuevos.
            List<DetalleMovimiento> detallesNuevos = detalles.stream()
                    .filter(d -> detallesExistentes.stream().noneMatch(de -> de.getId().equals(d.getId())))
                    .toList();

            orden.agregarDetalles(detallesNuevos);
            detallesNuevos.forEach(dn -> {
                try {
                    prodUbiDao.insertarProductoEnUbicacion(dn.getProducto(), dn.getUbicacion(), dn.getCantidad(), dn.esSalida());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            ordenDao.insertarDetalleOrdenMovimiento(detallesNuevos);

            // Busco los detalles a eliminar.
            List<DetalleMovimiento> detallesAEliminar = detallesExistentes.stream()
                    .filter(de -> detalles.stream().noneMatch(d -> d.getId().equals(de.getId())))
                    .toList();

            ordenDao.eliminarDetallesDeOrden(orderId, detallesAEliminar.stream().map(DetalleMovimiento::getId).toList());
            logger.info(String.format("Se eliminaron %d detalles de la orden %d", detalles.size(), orderId));

            logger.info(String.format("Se insertaron %d detalles a la orden %d", detalles.size(), orderId));
            ordenDao.actualizar(orden);
            logger.info("Orden de movimiento actualizada.");
        } catch (IllegalArgumentException | SQLException e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public void cancelarOrden(int id) {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            logger.info(String.format("Cancelando Orden de Movimiento: %d", id));
            OrdenMovimiento orden = ordenMovimientoDAO.buscarOrdenMovimientoPorId(id);
            if (orden == null) throw new RuntimeException("Orden no encontrada");
            orden.cancelar();
            ordenMovimientoDAO.actualizar(orden);
            logger.info(String.format("Orden de movimiento con Id %d cancelada correctamente", id));
        } catch (SQLException e) {
            logger.severe(String.format("Error al cancelar Orden de Movimiento: %d", id));
            throw new RuntimeException("Error al cancelar la orden de movimiento: " + e.getMessage(), e);
        }
    }
}
