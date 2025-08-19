package IG.domain.DAO;

import IG.domain.Clases.OrdenMovimiento;
import IG.domain.Enums.TipoMovimiento;
import main.java.IG.domain.Enums.OrdenMovimientoEstados;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenMovimientoDAO {

    private final Connection conn;

    public OrdenMovimientoDAO(Connection conn) {
        this.conn = conn;
    }

    // Inserta una nueva orden usando el nombre del enum en mayúsculas
    public void insertar(OrdenMovimiento nueva) throws SQLException {
        String sql = "INSERT INTO orden_movimiento (tipo, fecha, estado) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nueva.getTipo().name()); // nombre en mayúsculas
            stmt.setTimestamp(2, Timestamp.valueOf(nueva.getFecha()));
            stmt.setString(3, nueva.getEstado().name()); // ✅ devuelve "APROBADO", "PROCESO", etc. // nombre del enum en mayúsculas
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    nueva.setId(generatedKeys.getInt(1));
                }
            }
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
        o.setEstado(OrdenMovimientoEstados.valueOf(rs.getString("estado"))); // se obtiene directamente del nombre en mayúsculas
        return o;
    }
}
