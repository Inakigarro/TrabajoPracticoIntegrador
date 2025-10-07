package IG.domain.DAO;

import IG.domain.Clases.*;
import IG.domain.Enums.TipoMovimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenMovimientoDAO {

    private final Connection conn;

    public OrdenMovimientoDAO(Connection conn) {
        this.conn = conn;
    }

    public void inicializacion() throws SQLException {
        try {
            this.crearTablaOrdenMovimiento();
            this.crearTablaDetalleMovimiento();
        } catch (SQLException exception) {
            throw new SQLException("Error durante el proceso de inicializacion de OrdenMovimiento: " + exception.getMessage());
        }
    }

    public void insertarOrdenMovimiento(OrdenMovimiento ordenMovimiento) throws SQLException {
        String sql = "INSERT INTO orden_movimiento (tipo, fecha, estado) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var fecha = Timestamp.valueOf(ordenMovimiento.getFecha());
            stmt.setString(1, ordenMovimiento.getTipo().name()); // Usar el nombre del enum
            stmt.setTimestamp(2, fecha);
            stmt.setString(3, ordenMovimiento.getEstadoOrden().getEstado().getDescripcion());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordenMovimiento.setId(generatedKeys.getInt(1));
                }
            }
            catch (SQLException ex) {
                throw new SQLException("Error al obtener el ID generado: " + ex.getMessage(), ex);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar la orden de movimiento: " + ex.getMessage(), ex);
        }
    }

    public OrdenMovimiento buscarOrdenMovimientoPorId(Integer id) throws SQLException {
        String sql = """
                SELECT
                 om.id om_id,
                    om.tipo om_tipo,
                    om.fecha om_fecha,
                    om.estado om_estado
                FROM orden_movimiento om
                WHERE id = ?;
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Mapper.mapRowToOrdenMovimiento(rs);
                } else {
                    throw new SQLException("No se encontró una orden de movimiento con ID: " + id);
                }
            } catch (SQLException exception) {
                throw new SQLException("Error al buscar la orden de movimiento por ID: " + exception.getMessage(), exception);
            }
        } catch (SQLException exception) {
            throw new SQLException("Error al preparar la consulta para buscar la orden de movimiento por ID: " + exception.getMessage(), exception);
        }
    }

    public List<OrdenMovimiento> buscarOrdenMovimientoPorTipo(TipoMovimiento tipo) throws SQLException {
        String sql = """
                SELECT
                    om.id om_id,
                    om.tipo om_tipo,
                    om.fecha om_fecha,
                    om.estado om_estado
                FROM orden_movimiento om
                WHERE om.tipo = ?
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo.name()); // Usar el nombre del enum
            try (ResultSet rs = stmt.executeQuery()) {
                List<OrdenMovimiento> ordenMovimientos = new ArrayList<>();
                while (rs.next()) {
                    OrdenMovimiento om = Mapper.mapRowToOrdenMovimiento(rs);
                    ordenMovimientos.add(om);
                }
                return ordenMovimientos;
            } catch (SQLException exception) {
                String error = "Error al buscar la orden de movimiento por Tipo: ";
                System.out.println(error);
                throw new SQLException(error, exception.getMessage());
            }
        } catch (SQLException exception) {
            String error = "Error al preparar la consulta para buscar la orden de movimiento por Tipo: ";
            System.out.println(error);
            throw new SQLException(error, exception.getMessage());
        }
    }

    public List<OrdenMovimiento> listarOrdenMovimiento(Integer pageSize, Integer pageNumber) throws SQLException {
        String sql = """
                SELECT
                    om.id om_id,
                    om.tipo om_tipo,
                    om.fecha om_fecha,
                    om.estado om_estado
                FROM orden_movimiento om
                ORDER BY om.id
                LIMIT ? OFFSET ?
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageNumber - 1);
            try (ResultSet rs = stmt.executeQuery()) {
                List<OrdenMovimiento> ordenMovimientos = new ArrayList<>();
                while (rs.next()) {
                    OrdenMovimiento om = Mapper.mapRowToOrdenMovimiento(rs);
                    ordenMovimientos.add(om);
                }
                return ordenMovimientos;
            } catch (SQLException exception) {
                String error = "Error al listar las ordenes de movimiento: ";
                System.out.println(error);
                throw new SQLException(error, exception.getMessage());
            }
        } catch (SQLException exception) {
            String error = "Error al preparar la consulta para listar las ordenes de movimiento: ";
            System.out.println(error);
            throw new SQLException(error, exception.getMessage());
        }
    }

    public void insertarDetalleOrdenMovimiento(List<DetalleMovimiento> detallesMovimiento) throws SQLException {
        if (detallesMovimiento == null || detallesMovimiento.isEmpty()) return;
        String sql = "INSERT INTO detalle_movimiento (id_producto, cantidad, id_orden_movimiento, id_ubicacion, es_salida) VALUES (?, ?, ?, ?, ?)";
        Boolean autoCommitPrevio = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Integer pendientes = 0;

            for (DetalleMovimiento detalleMovimiento : detallesMovimiento) {



                stmt.setInt(1, detalleMovimiento.getProducto().getId());
                stmt.setDouble(2, detalleMovimiento.getCantidad());
                stmt.setInt(3, detalleMovimiento.getOrdenMovimiento().getId());
                stmt.setInt(4, detalleMovimiento.getUbicacion().getId());
                stmt.setBoolean(5, detalleMovimiento.esSalida());
                stmt.addBatch();
                pendientes++;

                if (pendientes % 100 == 0) {
                    stmt.executeBatch();
                }
            }

            stmt.executeBatch();
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar el detalle de la orden de movimiento: " + ex.getMessage(), ex);
        } finally {
            conn.setAutoCommit(autoCommitPrevio);
        }
    }

    public List<DetalleMovimiento> buscarDetallesPorOrdenId(Integer ordenId) throws SQLException {
        String sql = """
                SELECT
                	dm.id AS dm_id,
                	dm.cantidad AS dm_cantidad,
                	dm.id_producto AS dm_id_producto,
                	dm.id_orden_movimiento AS dm_id_orden_movimiento,
                	dm.id_ubicacion AS dm_id_ubicacion,
                	dm.es_salida AS dm_es_salida,
                	om.id AS om_id,
                	om.tipo AS om_tipo,
                	om.fecha AS om_fecha,
                	om.estado AS om_estado,
                	u.id AS u_id,
                	u.nro_estanteria AS u_nro_estanteria,
                	u.nro_nivel AS u_nro_nivel,
                	u.capacidad_usada AS u_capacidad_usada,
                	z.id AS z_id,
                	z.tipo AS z_tipo,
                	n.id AS n_id,
                	 pu.id AS pu_id,
                	 pu.stockProductoUbicacion AS pu_stockProductoUbicacion,
                	 p.id AS p_id,
                	 p.descripcion AS p_descripcion,
                	 p.cantidad_unidad AS p_cantidad_unidad,
                	 p.unidad_medida AS p_unidad_medida,
                	 p.stock AS p_stock,
                	 tp.id AS tp_id,
                	 tp.descripcion AS tp_descripcion
                FROM detalle_movimiento dm
                INNER JOIN orden_movimiento om ON dm.id_orden_movimiento = om.id
                INNER JOIN producto p ON dm.id_producto = p.id
                INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id
                INNER JOIN ubicacion u ON dm.id_ubicacion = u.id
                INNER JOIN zona z ON u.id_zona = z.id
                INNER JOIN nave n ON z.id_nave = n.id
                INNER JOIN producto_ubicacion pu ON pu.id_ubicacion = u.id
                WHERE dm.id_orden_movimiento = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ordenId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<DetalleMovimiento> detalles = new ArrayList<>();
                while (rs.next()) {
                    DetalleMovimiento dm = Mapper.mapRowToDetalleMovimiento(rs);
                    detalles.add(dm);
                }
                return detalles;
            } catch (SQLException exception) {
                String error = "Error al buscar los detalles por ID de orden: ";
                System.out.println(error);
                throw new SQLException(error, exception.getMessage());
            }
        } catch (SQLException exception) {
            String error = "Error al preparar la consulta para buscar los detalles por ID de orden: ";
            System.out.println(error);
            throw new SQLException(error, exception.getMessage());
        }
    }

    public void eliminarDetallesDeOrden(Integer ordenId, List<Integer> detalleIds) throws SQLException {
        if (detalleIds == null || detalleIds.isEmpty()) return;

        String sql = "DELETE FROM detalle_movimiento WHERE id_orden_movimiento = ? AND id = ?";
        Boolean autoCommitPrevio = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Integer pendientes = 0;

            for (Integer detalleId : detalleIds) {
                stmt.setInt(1, ordenId);
                stmt.setInt(2, detalleId);
                stmt.addBatch();
                pendientes++;

                if (pendientes % 100 == 0) {
                    stmt.executeBatch();
                }
            }

            stmt.executeBatch();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar los detalles de la orden de movimiento: " + ex.getMessage(), ex);
        } finally {
            conn.setAutoCommit(autoCommitPrevio);
        }
    }

    public void actualizar(OrdenMovimiento orden) throws SQLException {
        String sql = "UPDATE orden_movimiento SET tipo = ?, fecha = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orden.getTipo().name());
            stmt.setTimestamp(2, Timestamp.valueOf(orden.getFecha()));
            stmt.setString(3, orden.getEstado().getDescripcion());
            stmt.setInt(4, orden.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM orden_movimiento WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private void crearTablaOrdenMovimiento() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS orden_movimiento (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    tipo Enum('Sin Definir', 'Ingreso', 'Egreso', 'Interno') NOT NULL,
                    fecha TIMESTAMP NOT NULL,
                    estado Enum('Pendiente', 'Aprobado', 'En Proceso', 'Completado', 'Cancelado') NOT NULL
                );
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Creando tabla orden_movimiento...");
            stmt.execute();
            System.out.println("Tabla orden_movimiento creada correctamente.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla orden_movimiento: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage(), ex);
        }
    }

    private void crearTablaDetalleMovimiento() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS detalle_movimiento (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    cantidad DOUBLE NOT NULL,
                    id_producto INT NOT NULL,
                    id_orden_movimiento INT NOT NULL,
                    id_ubicacion INT NOT NULL,
                    es_salida BOOLEAN NOT NULL,
                    FOREIGN KEY (id_producto) REFERENCES producto(id),
                    FOREIGN KEY (id_orden_movimiento) REFERENCES orden_movimiento(id),
                    FOREIGN KEY (id_ubicacion) REFERENCES ubicacion(id)
                );
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Creando tabla detalle_movimiento...");
            stmt.execute();
            System.out.println("Tabla detalle_movimiento creada correctamente.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla detalle_movimiento: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage(), ex);
        }
    }
}
