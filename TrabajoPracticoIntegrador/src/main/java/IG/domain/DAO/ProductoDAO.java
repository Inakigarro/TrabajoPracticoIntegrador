package main.java.IG.domain.DAO;

import main.java.IG.domain.Clases.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private final Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (descripcion, unidad_medida, stock) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getDescripcion());
            stmt.setString(2, producto.getUnidadMedida());
            stmt.setDouble(3, producto.getStock());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1)); // Asigna el ID generado
                }
            }
        }
    }

    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProducto(rs);
                }
            }
        }
        return null;
    }

    public List<Producto> listarTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapRowToProducto(rs));
            }
        }
        return productos;
    }

    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET descripcion = ?, unidad_medida = ?, stock = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getDescripcion());
            stmt.setString(2, producto.getUnidadMedida());
            stmt.setDouble(3, producto.getStock());
            stmt.setInt(4, producto.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Producto mapRowToProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setUnidadMedida(rs.getString("unidad_medida"));
        p.setStock(rs.getDouble("stock"));
        return p;
    }
}
