package IG.domain.DAO;

import IG.domain.Clases.DetalleMovimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DetalleMovimientoDAO {
    private final Connection conn;

    public DetalleMovimientoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertar(DetalleMovimiento det, int idOrden) throws SQLException {
        String sql = "INSERT INTO detalle_movimiento (id_orden, id_producto_ubicacion, cantidad, es_salida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idOrden);
            stmt.setInt(2, det.getProducto().getId());
            stmt.setDouble(3, det.getCantidad());

            // ⚠️ Asegurate de que tu clase DetalleMovimiento tenga isEsSalida() o getEsSalida()
            stmt.setBoolean(4, det.esSalida());

            stmt.executeUpdate();
        }
    }
}