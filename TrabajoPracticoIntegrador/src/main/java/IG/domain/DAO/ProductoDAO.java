package IG.domain.DAO;

import IG.domain.Clases.Producto;
import IG.domain.Clases.TipoProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductoDAO es una clase de acceso a datos (DAO) para la entidad Producto.
 * Proporciona métodos para realizar operaciones CRUD (crear, leer, actualizar y eliminar)
 * sobre la tabla 'producto' en la base de datos.
 *
 * Métodos principales:
 * - insertar(Producto producto): Inserta un nuevo producto en la base de datos.
 * - buscarPorId(int id): Busca y devuelve un producto por su ID.
 * - listarTodos(): Devuelve una lista con todos los productos de la base de datos.
 * - actualizar(Producto producto): Actualiza los datos de un producto existente.
 * - eliminar(int id): Elimina un producto por su ID.
 *
 * Utiliza una conexión JDBC proporcionada externamente.
 */
public class ProductoDAO {

    // Conexión a la base de datos
    private final Connection conn;

    /**
     * Constructor de ProductoDAO.
     *
     * @param conn Conexión JDBC a la base de datos.
     */
    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertarTipoProducto(TipoProducto tipoProducto) {
        String tipoProductoSql = "INSERT INTO tipo_producto (descripcion) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(tipoProductoSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipoProducto.getDescripcion());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el tipo de producto, no se afectaron filas.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tipoProducto.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el tipo de producto.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar tipo de producto: " + ex.getMessage());
        }
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     * Asigna al objeto producto el ID generado automáticamente.
     *
     * @param producto El producto a insertar.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
    public void insertarProducto(Producto producto) throws SQLException {
        String productoSql = "INSERT INTO producto (descripcion, cantidad_unidad, unidad_medida, stock, id_tipo_producto) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(productoSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getDescripcion());
            stmt.setDouble(2, producto.getCantidadUnidad());
            stmt.setString(3, producto.getUnidadMedida());
            stmt.setDouble(4, producto.getStock());
            stmt.setInt(5, producto.getTipoProducto().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el producto, no se afectaron filas.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el producto.");
                }
            }
        }
    }

    /**
     * Busca un producto en la base de datos por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El producto encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
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

    /**
     * Devuelve una lista con todos los productos de la base de datos.
     *
     * @return Lista de productos.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
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

    /**
     * Actualiza los datos de un producto existente en la base de datos.
     *
     * @param producto El producto con los nuevos datos.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
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

    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * @param id El ID del producto a eliminar.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Convierte una fila de ResultSet en un objeto Producto.
     *
     * @param rs ResultSet posicionado en la fila del producto.
     * @return Objeto Producto correspondiente a la fila.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
    private Producto mapRowToProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setUnidadMedida(rs.getString("unidad_medida"));
        p.setStock(rs.getDouble("stock"));
        return p;
    }
}