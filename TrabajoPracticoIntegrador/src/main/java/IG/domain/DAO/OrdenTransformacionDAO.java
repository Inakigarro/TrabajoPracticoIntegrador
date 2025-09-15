package IG.domain.DAO;

import IG.domain.Clases.DetalleTransformacion;
import IG.domain.Clases.OrdenTransformacion;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoTransformacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/// <summary>
/// DAO utilizado para gestionar las operaciones CRUD de la entidad OrdenTransformacion
/// y DetalleOrdenTransformacion.
/// </summary>
public class OrdenTransformacionDAO {

    private final Connection conn;

    /// <summary>
    /// Constructor del DAO, recibe la conexión a la base de datos.
    /// </summary>
    /// <param name="conn">Conexión a la base de datos</param>
    public OrdenTransformacionDAO(Connection conn) {
        this.conn = conn;
    }

    /// <summary>
    /// Inicializa la base de datos creando las tablas necesarias si no existen.
    /// </summary>
    /// <throws>SQLException si ocurre algún error durante la creación de tablas</throws>
    public void inicializacion() throws SQLException {
        try {
            this.crearTablaOrdenTransformacion();
            this.crearTablaDetalleTransformacion();
        } catch (SQLException exception) {
            throw new SQLException("Error durante el proceso de inicializacion de OrdenTransformacion: " + exception.getMessage());
        }
    }

    /// <summary>
    /// Inserta una nueva orden de transformación en la base de datos.
    /// </summary>
    /// <param name="ordenTransformacion">Objeto de tipo OrdenTransformacion a insertar</param>
    /// <throws>SQLException si ocurre algún error durante la inserción</throws>
    public void insertarOrdenTransformacion(OrdenTransformacion ordenTransformacion) throws SQLException {
        String sql = "INSERT INTO orden_transformacion (tipo, fecha, estado) VALUES (?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var fecha = Timestamp.valueOf(ordenTransformacion.getFecha());

            ps.setString(1, ordenTransformacion.getTipo().getDescripcion());
            ps.setTimestamp(2, fecha);
            ps.setString(3, ordenTransformacion.getEstado().getDescripcion());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordenTransformacion.setId(generatedKeys.getInt(1));
                }
            }
            catch (SQLException ex){
                throw new SQLException("Error al obtener el ID generado: " + ex.getMessage(), ex);
            }
        } catch (SQLException ex){
            throw new SQLException("Error al insertar la orden de transformacion: " + ex.getMessage(), ex);
        }
    }

    /// <summary>
    /// Busca una orden de transformación por su ID.
    /// </summary>
    /// <param name="id">ID de la orden de transformación</param>
    /// <returns>OrdenTransformacion encontrada</returns>
    /// <throws>SQLException si no se encuentra la orden o ocurre un error en la consulta</throws>
    public OrdenTransformacion buscarOrdenTransformacionPorId(Integer id) throws SQLException {
        String sql = """
            SELECT
                ot.id ot_id,
                ot.tipo ot_tipo,
                ot.fecha ot_fecha,
                ot.estado ot_estado
            FROM orden_transformacion ot
            WHERE ot.id = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrdenTransformacion ot = new OrdenTransformacion();
                    ot.setId(rs.getInt("ot_id"));
                    ot.setTipo(TipoTransformacion.valueOf(rs.getString("ot_tipo")));
                    ot.setFecha(rs.getTimestamp("ot_fecha").toLocalDateTime());
                    ot.setEstado(EstadosOrdenes.valueOf(rs.getString("ot_estado")));

                    return ot;
                } else {
                    throw new SQLException("No se encontró la orden de transformación con ID: " + id);
                }
            }
        }
    }

    /// <summary>
    /// Inserta una lista de detalles de transformación asociados a una orden.
    /// </summary>
    /// <param name="detalleTransformacion">Lista de DetalleTransformacion a insertar</param>
    /// <throws>SQLException si ocurre un error durante la inserción</throws>
    public void insertarDetalleTransformacion(List<DetalleTransformacion> detalleTransformacion) throws SQLException {
        String sql = "INSERT INTO detalle_transformacion (id_producto, cantidad, id_orden_transformacion, id_ubicacion) VALUES (?, ?, ?, ?, ?)";
        Boolean autoCommitPrevio = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Integer pendientes = 0;

            for (DetalleTransformacion detalleTransformacionDetalle : detalleTransformacion) {
                ps.setInt(1, detalleTransformacionDetalle.getProducto().getId());
                ps.setDouble(2, detalleTransformacionDetalle.getCantidad());
                ps.setInt(3, detalleTransformacionDetalle.getOrdenTransformacion().getId());
                ps.setInt(4, detalleTransformacionDetalle.getUbicacion().getId());
                ps.executeUpdate();
                pendientes++;

                if(pendientes % 100 == 0){
                    ps.executeBatch();
                }
            }

            ps.executeBatch();
        } catch (SQLException ex){
            throw new SQLException("Error al insertar el detalle de la orden de transformacion: " + ex.getMessage(), ex);
        }
    }

    /// <summary>
    /// Busca una orden de transformación por su ID.
    /// </summary>
    /// <param name="id">ID de la orden</param>
    /// <returns>OrdenTransformacion encontrada, o null si no existe</returns>
    /// <throws>SQLException si ocurre un error en la consulta</throws>
    public OrdenTransformacion buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM orden_transformacion WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToOrdenTransformacion(rs);
                }
            }
        }
        return null;
    }

    /// <summary>
    /// Lista todas las órdenes de transformación de la base de datos.
    /// </summary>
    /// <returns>Lista de OrdenTransformacion</returns>
    /// <throws>SQLException si ocurre un error en la consulta</throws>
    public List<OrdenTransformacion> listarTodos() throws SQLException{
        List<OrdenTransformacion> ordenes = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM orden_transformacion");
            while (rs.next()) {
                ordenes.add(mapRowToOrdenTransformacion(rs));
            }
        }
        return ordenes;
    }

    /// <summary>
    /// Actualiza los datos de una orden de transformación existente.
    /// </summary>
    /// <param name="orden">OrdenTransformacion con los datos actualizados</param>
    /// <throws>SQLException si ocurre un error durante la actualización</throws>
    public void actualizar (OrdenTransformacion orden) throws SQLException{
        String sql = "UPDATE orden_transformacion SET tipo = ?, fecha = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orden.getTipo().name());
            ps.setTimestamp(2, Timestamp.valueOf(orden.getFecha()));
            ps.setString(3, orden.getEstado().name());
            ps.setInt(4, orden.getId());
            ps.executeUpdate();
        }
    }

    /// <summary>
    /// Elimina una orden de transformación por su ID.
    /// </summary>
    /// <param name="id">ID de la orden a eliminar</param>
    /// <throws>SQLException si ocurre un error durante la eliminación</throws>
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM orden_transformacion WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /// <summary>
    /// Mapea un registro del ResultSet a un objeto OrdenTransformacion.
    /// </summary>
    /// <param name="rs">ResultSet con los datos</param>
    /// <returns>Objeto OrdenTransformacion</returns>
    /// <throws>SQLException si ocurre un error al leer los datos</throws>
    private OrdenTransformacion mapRowToOrdenTransformacion (ResultSet rs) throws SQLException {
        OrdenTransformacion o = new OrdenTransformacion();
        o.setId(rs.getInt("id"));
        o.setTipo(TipoTransformacion.values()[rs.getInt("tipo")]);
        o.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        o.setEstado(EstadosOrdenes.valueOf(rs.getString("estado")));
        return o;
    }

    /// <summary>
    /// Crea la tabla orden_transformacion si no existe.
    /// </summary>
    /// <throws>SQLException si ocurre un error durante la creación de la tabla</throws>
    private void crearTablaOrdenTransformacion() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS orden_transformacion (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    tipo Enum('Sin Definir', 'Ingreso', 'Egreso', 'Interno') NOT NULL,
                    fecha TIMESTAMP NOT NULL,
                    estado Enum('Pendiente', 'Aprobado', 'En Proceso', 'Completado', 'Cancelado') NOT NULL
                );
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Creando tabla orden_transformacion...");
            ps.execute();
            System.out.println("Tabla orden_transformacion creada correctamente");
        } catch (SQLException ex) {
            String error = "Error al crear el tabla orden_transformacion: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage(), ex);
        }
    }

    /// <summary>
    /// Crea la tabla detalle_transformacion si no existe.
    /// </summary>
    /// <throws>SQLException si ocurre un error durante la creación de la tabla</throws>
    public void crearTablaDetalleTransformacion() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS detalle_transformacion (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    cantidad DOUBLE NOT NULL,
                    id_producto INT NOT NULL,
                    id_orden_transformacion INT NOT NULL,
                    id_ubicacion INT NOT NULL,
                    es_salida BOOLEAN NOT NULL,
                    FOREIGN KEY (id_producto) REFERENCES producto(id),
                    FOREIGN KEY (id_orden_transformacion) REFERENCES orden_transformacion(id),
                    FOREIGN KEY (id_ubicacion) REFERENCES ubicacion(id)
                );
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Creando tabla detalle_transformacion...");
            ps.execute();
            System.out.println("Tabla detalle_transformacion creada correctamente");
        } catch (SQLException ex) {
            String error = "Error al crear el detalle transformacion: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage(), ex);
        }
    }

    /// <summary>
    /// Inserta un nuevo detalle de transformación asociado a una orden de transformación.
    /// El ID de la orden se pasa como parámetro y se asigna al detalle.
    /// </summary>
    /// <param name="detalle">Objeto DetalleTransformacion a insertar.</param>
    /// <param name="idOrden">ID de la orden de transformación asociada.</param>
    /// <exception cref="SQLException">Se lanza si ocurre un error durante la inserción en la base de datos.</exception>
    public void insertarDetalleTransformacion(DetalleTransformacion detalle, int idOrden) throws SQLException {
        String sql = "INSERT INTO detalle_transformacion (id_producto, cantidad, id_orden_transformacion, id_ubicacion, es_salida) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getProducto().getId());
            ps.setDouble(2, detalle.getCantidad());
            ps.setInt(3, idOrden);
            ps.setInt(4, detalle.getUbicacion().getId());
            ps.setBoolean(5, detalle.isEsSalida());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Si necesitás asignar un ID al detalle, podrías agregar un setId() en DetalleTransformacion
                    // detalle.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar el detalle de la orden de transformación: " + ex.getMessage(), ex);
        }
    }

}

