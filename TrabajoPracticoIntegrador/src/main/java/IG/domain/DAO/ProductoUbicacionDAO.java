package IG.domain.DAO;

import IG.domain.Clases.*;
import IG.domain.Constants.UbicacionConstants;
import IG.domain.Enums.TipoZona;
import IG.persistence.ProductoUbicacionQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoUbicacionDAO {
    private final Connection connection;

    public ProductoUbicacionDAO(Connection connection) {
        this.connection = connection;
    }

    public void inicializacion() throws SQLException{
        try {
            this.crearTablaTipoProducto();
            this.crearTablaProducto();
            this.crearTablaNave();
            this.crearTablaZona();
            this.crearTablaUbicacion();
            this.crearTablaProductoUbicacion();
        } catch (SQLException ex) {
            System.out.println("Error al inicializar las tablas: " + ex.getMessage());
        }
    }

    // Operaciones TipoProducto.
    public TipoProducto insertarTipoProducto(String descripcion) throws SQLException {
        String tipoProductoSql = "INSERT INTO tipo_producto (descripcion) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(tipoProductoSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, descripcion);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el tipo de producto, no se afectaron filas.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    TipoProducto tipoProducto = new TipoProducto(descripcion);
                    tipoProducto.setId(generatedKeys.getInt(1));
                    return tipoProducto;
                } else {
                    throw new SQLException("No se pudo insertar el tipo de producto, no se obtuvo el ID.");
                }
            } catch (SQLException ex) {
                throw new SQLException("Error al obtener el ID generado para el tipo de producto: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar tipo de producto: " + ex.getMessage());
        }
    }

    public List<TipoProducto> listarTodosTiposProductos() {
        List<TipoProducto> tiposProductos = new ArrayList<>();

        String sql = """
                SELECT
                    tp.id AS tp_id,
                    tp.descripcion AS tp_descripcion
                FROM tipo_producto tp
                """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TipoProducto tipoProducto = Mapper.mapRowToTipoProducto(rs);
                tiposProductos.add(tipoProducto);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar los tipos de productos: " + ex.getMessage());
        }

        return tiposProductos;
    }

    // Operaciones Producto.
    public Producto insertarProducto(Producto producto) throws SQLException {
        String productoSql = "INSERT INTO producto (descripcion, cantidad_unidad, unidad_medida, stock, id_tipo_producto) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(productoSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getDescripcion());
            stmt.setDouble(2, producto.getCantidadUnidad());
            stmt.setString(3, producto.getUnidadMedida().getDescripcion());
            stmt.setDouble(4, producto.getStock());
            stmt.setInt(5, producto.getTipoProducto().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el producto, no se afectaron filas.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                    return producto;
                } else {
                    throw new SQLException("No se pudo insertar el producto, no se obtuvo el ID.");
                }
            } catch (SQLException ex) {
                throw  new SQLException("Error al insertar el producto: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar el producto: " + ex.getMessage());
        }
    }

    public List<Producto> listarTodosProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = ProductoUbicacionQueries.listarProductos;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto producto = Mapper.mapRowToProducto(rs);
                productos.add(producto);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar los productos: " + ex.getMessage());
        }
        return productos;
    }

    // Operaciones Nave.
    public Nave insertarNave(Nave nave) throws SQLException {
        String sql = "INSERT INTO nave (id) VALUES (DEFAULT)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar la nave, no se afectaron filas.");
            } else {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nave.setId(generatedKeys.getInt(1));
                        return nave;
                    } else {
                        throw new SQLException("No se pudo insertar la nave, no se obtuvo el ID.");
                    }
                } catch (SQLException ex) {
                    throw new SQLException("Error al obtener el ID generado para la nave: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar la nave: " + ex.getMessage());
        }
    }

    public List<Nave> listarNaves() throws SQLException {
        List<Nave> naves = new ArrayList<>();
        String sql = ProductoUbicacionQueries.listarNaves;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Nave nave = Mapper.mapRowToNave(rs);
                    naves.add(nave);
                }
                return naves;
            } catch (SQLException ex) {
                throw new SQLException("Error al listar las naves: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las naves: " + ex.getMessage());
        }
    }

    // Operaciones Zona.
    public Zona insertarZona(Zona zona) throws SQLException {
        String sql = """
                INSERT INTO zona (tipo, id_nave)
                VALUES (?, ?)
                """;

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, zona.getTipo().getDescripcion());
            stmt.setInt(2, zona.getNave().getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar la zona, no se afectaron filas.");
            } else {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        zona.setId(generatedKeys.getInt(1));
                        return zona;
                    } else {
                        throw new SQLException("No se pudo insertar la zona, no se obtuvo el ID.");
                    }
                } catch (SQLException ex) {
                    throw new SQLException("Error al obtener el ID generado para la zona: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar la zona: " + ex.getMessage());
        }
    }

    public Zona buscarZonaPorId(Integer id) throws SQLException {
        String sql = """
                SELECT
                    z.id as z_id,
                    z.tipo as z_tipo_zona,
                    n.id as n_id
                FROM zona z
                INNER JOIN nave n ON z.id_nave = n.id
                WHERE z.id = ?
                """;

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
               stmt.setInt(1, id);
               try (ResultSet rs = stmt.executeQuery()) {
                   if (rs.next()) {
                       Zona zona = new Zona();
                       zona.setId(rs.getInt("z_id"));
                       zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo_zona").toUpperCase()));

                       Nave nave = new Nave();
                       nave.setId(rs.getInt("n_id"));
                       zona.setNave(nave);
                       nave.agregarZona(zona);

                       return zona;
                   } else {
                       throw new SQLException("No se encontró la zona con ID: " + id);
                   }
               } catch (SQLException ex) {
                   throw new SQLException("Error al obtener la zona: " + ex.getMessage());
               }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener la zona: " + ex.getMessage());
        }
    }

    public List<Zona> buscarZonasPorNaveId(Integer naveId) throws SQLException {
        String sql = ProductoUbicacionQueries.listarZonasPorNaveId;

        List<Zona> zonas = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, naveId);
               try (ResultSet rs = stmt.executeQuery()) {
                   while (rs.next()) {
                       Zona zona = Mapper.mapRowToZona(rs);
                       zonas.add(zona);
                   }
                   return zonas;
               } catch (SQLException ex) {
                   throw new SQLException("Error al obtener las zonas: " + ex.getMessage());
               }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener las zonas: " + ex.getMessage());
        }
    }

    // Operaciones Ubicacion.
    public Ubicacion insertarUbicacion(Ubicacion ubicacion) throws SQLException{
        String sql = """
                INSERT INTO ubicacion (nro_estanteria, nro_nivel, capacidad_usada, id_zona)
                VALUES (?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ubicacion.getNroEstanteria());
            stmt.setInt(2, ubicacion.getNroNivel());
            stmt.setDouble(3, ubicacion.getCapacidadUsada());
            stmt.setInt(4, ubicacion.getZona().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar la ubicación, no se afectaron filas.");
            } else {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ubicacion.setId(generatedKeys.getInt(1));
                        return ubicacion;
                    } else {
                        throw new SQLException("No se pudo insertar la ubicación, no se obtuvo el ID.");
                    }
                } catch (SQLException ex) {
                    throw new SQLException("Error al obtener el ID generado para la ubicación: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al insertar la ubicación: " + ex.getMessage());
        }
    }

    public List<Ubicacion> listarTodasUbicaciones() throws SQLException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = ProductoUbicacionQueries.listarUbicaciones;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ubicacion ubicacion = Mapper.mapRowToUbicacion(rs);
                ubicaciones.add(ubicacion);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las ubicaciones: " + ex.getMessage());
        }
        return ubicaciones;
    }

    public List<ProductoUbicacion> listarProductosPorUbicacion(Integer ubicacionId) throws SQLException {
        List<ProductoUbicacion> productos = new ArrayList<>();
        String sql = ProductoUbicacionQueries.listarProductosPorUbicacion;
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ubicacionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductoUbicacion productoUbicacion = Mapper.mapRowToProductoUbicacionSinUbicacion(rs);
                productos.add(productoUbicacion);
            }

            return productos;
        } catch (SQLException ex) {
            throw new SQLException("Error al listar los productos en la ubicacion: " + ex.getMessage());
        }
    }

    public List<Ubicacion> listarUbicacionesPorProducto(Integer productoId) throws SQLException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = ProductoUbicacionQueries.listarUbicacionesProProducto;

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ubicacion ubicacion = Mapper.mapRowToUbicacion(rs);
                ubicaciones.add(ubicacion);
            }

            return ubicaciones;
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las ubicaciones: " + ex.getMessage());
        }
    }

    public void insertarProductoEnUbicacion(Producto producto, Ubicacion ubicacion, double cantidad, boolean esSalida) throws SQLException {
        try {
            connection.setAutoCommit(false);
            String sql = "";
            // Si es salida, se asume que existe el producto en la ubicacion y se resta el stock de esa ubicacion.
            if (esSalida) {
                sql = """
                        UPDATE producto_ubicacion
                        SET stockProductoUbicacion = stockProductoUbicacion - ?
                        WHERE id_producto = ? AND id_ubicacion = ?
                        """;

                try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setDouble(1, cantidad);
                    stmt.setInt(2, producto.getId());
                    stmt.setInt(3, ubicacion.getId());
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("No se pudo actualizar el stock del producto en la ubicación, no se afectaron filas.");
                    }
                } catch (SQLException ex) {
                    throw new SQLException("Error al actualizar el stock del producto en la ubicación: " + ex.getMessage());
                }
            } else {
                // Si no, se inserta una nueva fila o se actualiza el stock en una ubicacion que ya tenga el producto.
                sql = """
                """;
            }

            String sqlPU = "INSERT INTO producto_ubicacion (id_producto, id_ubicacion, stockProductoUbicacion) VALUES (?, ?, ?) ";

            if (esSalida){
                sqlPU += "ON DUPLICATE KEY UPDATE stockProductoUbicacion =stockProductoUbicacion - ?";
            } else {
                sqlPU += "ON DUPLICATE KEY UPDATE stockProductoUbicacion =stockProductoUbicacion + ?";
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlPU)) {
                ps.setInt(1, producto.getId());
                ps.setInt(2, ubicacion.getId());
                ps.setDouble(3, cantidad);
                ps.setDouble(4, cantidad);
                ps.executeUpdate();
            }

            String sqlProducto = "UPDATE producto SET stock = stock + ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlProducto)) {
                ps.setDouble(1, cantidad);
                ps.setInt(2, producto.getId());
                ps.executeUpdate();
            }

            String sqlUbicacion = "UPDATE ubicacion SET capacidad_usada = capacidad_usada + ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlUbicacion)) {
                ps.setDouble(1, cantidad);
                ps.setInt(2, ubicacion.getId());
                ps.executeUpdate();
            }

            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void actualizarProductoUbicacion(ProductoUbicacion productoUbicacion) throws SQLException {
        String sql = """
                    UPDATE producto_ubicacion
                    set stockProductoUbicacion = ?
                    WHERE id = ?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, productoUbicacion.getStockProductoUbicacion());
            stmt.setInt(2, productoUbicacion.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo actualizar el producto en la ubicación, no se afectaron filas.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el producto en la ubicación: " + ex.getMessage());
        }
    }

    // Operaciones de inicializacion.
    private void crearTablaTipoProducto() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS tipo_producto(
                id INT AUTO_INCREMENT PRIMARY KEY,
                descripcion VARCHAR(255)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            System.out.println("Creando tabla tipo_producto si no existe...");
            stmt.execute(sql);
            System.out.println("Tabla tipo_producto creada o ya existía.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla tipo_producto. ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    private void crearTablaProducto() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS producto(
                id INT AUTO_INCREMENT PRIMARY KEY,
                descripcion VARCHAR(150),
                cantidad_unidad DOUBLE,
                unidad_medida VARCHAR(255),
                stock DOUBLE DEFAULT 0,
                id_tipo_producto INT,
                FOREIGN KEY (id_tipo_producto) REFERENCES tipo_producto(id)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            System.out.println("Creando tabla producto si no existe...");
            stmt.execute(sql);
            System.out.println("Tabla producto creada o ya existía.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla producto. ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    private void crearTablaNave() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS nave (
                id INT AUTO_INCREMENT PRIMARY KEY
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            System.out.println("Creando tabla nave si no existe...");
            stmt.execute(sql);
            System.out.println("Tabla nave creada o ya existía.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla nave. ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    private void crearTablaZona() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS zona(
                id INT AUTO_INCREMENT PRIMARY KEY,
                tipo ENUM('Entrada', 'Salida', 'Transformacion', 'Almacenamiento') NOT NULL,
                id_nave INT,
                FOREIGN KEY (id_nave) REFERENCES nave(id)
            );
        """;

        try(Statement stmt = connection.createStatement()) {
            System.out.println("Creando tabla zona si no existe...");
            stmt.execute(sql);
            System.out.println("Tabla zona creada o ya existía.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla zona. ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    private void crearTablaUbicacion() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS ubicacion(
                id INT AUTO_INCREMENT PRIMARY KEY,
                nro_estanteria INT NOT NULL,
                nro_nivel INT NOT NULL,
                capacidad_usada DOUBLE DEFAULT 0,
                id_zona INT,
                FOREIGN KEY (id_zona) REFERENCES zona(id)
            );
        """;

        try(Statement stmt = connection.createStatement()) {
            System.out.println("Creando tabla ubicacion si no existe...");
            stmt.execute(sql);
            System.out.println("Tabla ubicacion creada o ya existía.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla ubicacion. ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    private void crearTablaProductoUbicacion() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS producto_ubicacion(
                id INT AUTO_INCREMENT PRIMARY KEY,
                id_producto INT,
                id_ubicacion INT,
                stockProductoUbicacion DOUBLE DEFAULT 0,
                FOREIGN KEY (id_producto) REFERENCES producto(id),
                FOREIGN KEY (id_ubicacion) REFERENCES ubicacion(id)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            System.out.println("Creando tabla producto_ubicacion si no existe...");
            stmt.execute(sql);
            System.out.println("Tabla producto_ubicacion creada o ya existía.");
        } catch (SQLException ex) {
            String error = "Error al crear la tabla producto_ubicacion. ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }
}
