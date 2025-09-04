package IG.domain.DAO;

import IG.domain.Clases.DetalleMovimiento;
import IG.domain.Clases.OrdenMovimiento;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoMovimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/// <summary>
/// DAO utilizado para gestionar las operaciones CRUD de la entidad OrdenMovimiento
/// y DetalleOrdenMovimiento.
/// </summary>
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

    // Operaciones Orden Movimiento.

    /// <summary>
    /// Inserta una nueva orden de movimiento en la base de datos.
    /// El ID se genera automáticamente y se asigna al objeto OrdenMovimiento proporcionado.
    /// </summary>
    public void insertarOrdenMovimiento(OrdenMovimiento ordenMovimiento) throws SQLException {

        // Inicializo la query a ejecutar.
        String sql = "INSERT INTO orden_movimiento (tipo, fecha, estado) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var fecha = Timestamp.valueOf(ordenMovimiento.getFecha());
            stmt.setString(1, ordenMovimiento.getTipo().getDescripcion()); // nombre en mayúsculas
            stmt.setTimestamp(2, fecha);
            stmt.setString(3, ordenMovimiento.getEstado().getDescripcion()); // ✅ devuelve "APROBADO", "PROCESO", etc. // nombre del enum en mayúsculas
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
                WHERE id = 1;
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrdenMovimiento om = new OrdenMovimiento();
                    om.setId(rs.getInt("om_id"));
                    om.setTipo(TipoMovimiento.valueOf(rs.getString("om_tipo")));
                    om.setFecha(rs.getTimestamp("om_fecha").toLocalDateTime());
                    om.setEstado(EstadosOrdenes.valueOf(rs.getString("om_estado")));

                    return om;
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
    /// <summary>
    /// Inserta un nuevo detalle de orden de movimiento en la base de datos.
    /// El ID se genera automáticamente y se asigna al objeto DetalleMovimiento proporcionado.
    /// </summary>
    public void insertarDetalleOrdenMovimiento(List<DetalleMovimiento> detallesMovimiento) throws SQLException {
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

                // Se ejecutan batches pequeños para evitar saturar la memoria.
                if (pendientes % 100 == 0) {
                    stmt.executeBatch();
                }
            }

            // Ejecutar los pendientes restantes.
            stmt.executeBatch();
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar el detalle de la orden de movimiento: " + ex.getMessage(), ex);
        }
    }

    public OrdenMovimiento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM orden_movimiento WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRowToOrdenMovimiento(rs);
            }
        }
        return null;
    }

    public List<OrdenMovimiento> listarTodos() throws SQLException {
        List<OrdenMovimiento> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM orden_movimiento";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ordenes.add(mapRowToOrdenMovimiento(rs));
            }
        }
        return ordenes;
    }

    public void actualizar(OrdenMovimiento orden) throws SQLException {
        String sql = "UPDATE orden_movimiento SET tipo = ?, fecha = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orden.getTipo().name());
            stmt.setTimestamp(2, Timestamp.valueOf(orden.getFecha()));
            stmt.setString(3, orden.getEstado().name());
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

    private OrdenMovimiento mapRowToOrdenMovimiento(ResultSet rs) throws SQLException {
        OrdenMovimiento o = new OrdenMovimiento();
        o.setId(rs.getInt("id"));
        o.setTipo(TipoMovimiento.valueOf(rs.getString("tipo")));
        o.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        o.setEstado(EstadosOrdenes.valueOf(rs.getString("estado"))); // se obtiene directamente del nombre en mayúsculas
        return o;
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
