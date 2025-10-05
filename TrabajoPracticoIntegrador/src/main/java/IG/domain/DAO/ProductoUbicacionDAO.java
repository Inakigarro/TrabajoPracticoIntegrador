package IG.domain.DAO;

import IG.domain.Clases.*;
import IG.domain.Enums.TipoZona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/// <summary>
/// Clase utilizada para gestion de operaciones para las tablas
/// Producto, Ubicacion y ProductoUbicacion.
/// </summary>
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

    public TipoProducto buscarTipoProductoPorId(Integer id)  throws SQLException {
        String sql = "SELECT * FROM tipo_producto WHERE id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
               ps.setInt(1, id);
               try (ResultSet rs = ps.executeQuery()) {
                   if (rs.next()) {
                       TipoProducto tipoProducto = new TipoProducto();
                       tipoProducto.setId(rs.getInt(1));
                       tipoProducto.setDescripcion(rs.getString(2));
                       return tipoProducto;
                   }
               } catch (SQLException ex) {
                   throw new SQLException("Error al obtener el tipo de producto: " + ex.getMessage());
               }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el tipo de producto: " + ex.getMessage());
        }
        return null;
    }

    public List<TipoProducto> listarTodosTiposProductos() {
        List<TipoProducto> tiposProductos = new ArrayList<>();

        String sql = "SELECT * FROM tipo_producto";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TipoProducto tipoProducto = new TipoProducto();
                tipoProducto.setId(rs.getInt(1));
                tipoProducto.setDescripcion(rs.getString(2));
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

    public Producto buscarProductoPorId(int id) throws SQLException {
        String sql = """
            SELECT 
                p.id,
                p.descripcion,
                p.cantidad_unidad,
                p.unidad_medida,
                p.stock,
                tp.id,
                tp.descripcion
            FROM producto p
            INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id
            WHERE id = ?""";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("p_id"));
                    producto.setDescripcion(rs.getString("p_descripcion"));
                    producto.setCantidadUnidad(rs.getDouble("p_cantidad_unidad"));
                    producto.setUnidadMedida(rs.getString("p_unidad_medida"));
                    producto.setStock(rs.getDouble("p_stock"));

                    TipoProducto tipoProducto = new TipoProducto();
                    tipoProducto.setId(rs.getInt("tp_id"));
                    tipoProducto.setDescripcion(rs.getString("tp_descripcion"));
                    producto.setTipoProducto(tipoProducto);

                    return producto;
                } else {
                    throw new SQLException("No se encontró el producto con ID: " + id);
                }
            } catch (SQLException ex) {
                throw new SQLException("Error al obtener el producto: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el producto: " + ex.getMessage());
        }
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = """
            UPDATE producto
            SET 
                descripcion = ?,
                cantidad_unidad = ?,
                unidad_medida = ?,
                stock = ?,
                id_tipo_producto = ?
            WHERE id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getDescripcion());
            stmt.setDouble(2, producto.getCantidadUnidad());
            stmt.setString(3, producto.getUnidadMedida());
            stmt.setDouble(4, producto.getStock());
            stmt.setInt(5, producto.getTipoProducto().getId());
            stmt.setInt(6, producto.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo actualizar el producto, no se afectaron filas.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el producto: " + ex.getMessage());
        }
    }

    public List<Producto> listarProductos(Integer pageNumber, Integer pageSize) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT 
                p.id AS p_id,
                p.descripcion AS p_descripcion,
                p.cantidad_unidad AS p_cantidad_unidad,
                p.unidad_medida AS p_unidad_medida,
                p.stock AS p_stock,
                tp.id AS tp_id,
                tp.descripcion AS tp_descripcion
            FROM producto p
            INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id
            ORDER BY p.id
            LIMIT ? OFFSET ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (pageNumber - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("p_id"));
                producto.setDescripcion(rs.getString("p_descripcion"));
                producto.setCantidadUnidad(rs.getDouble("p_cantidad_unidad"));
                producto.setUnidadMedida(rs.getString("p_unidad_medida"));
                producto.setStock(rs.getDouble("p_stock"));

                TipoProducto tipoProducto = new TipoProducto();
                tipoProducto.setId(rs.getInt("tp_id"));
                tipoProducto.setDescripcion(rs.getString("tp_descripcion"));
                producto.setTipoProducto(tipoProducto);

                productos.add(producto);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar los productos: " + ex.getMessage());
        }
        return productos;
    }

    public List<Producto> listarTodosProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT 
                p.id AS p_id,
                p.descripcion AS p_descripcion,
                p.cantidad_unidad AS p_cantidad_unidad,
                p.unidad_medida AS p_unidad_medida,
                p.stock AS p_stock,
                tp.id AS tp_id,
                tp.descripcion AS tp_descripcion
            FROM producto p
            INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id
            ORDER BY p.id
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("p_id"));
                producto.setDescripcion(rs.getString("p_descripcion"));
                producto.setCantidadUnidad(rs.getDouble("p_cantidad_unidad"));
                producto.setUnidadMedida(rs.getString("p_unidad_medida"));
                producto.setStock(rs.getDouble("p_stock"));

                TipoProducto tipoProducto = new TipoProducto();
                tipoProducto.setId(rs.getInt("tp_id"));
                tipoProducto.setDescripcion(rs.getString("tp_descripcion"));
                producto.setTipoProducto(tipoProducto);

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

    public Nave buscarNavePorId(Integer id) throws SQLException {
        String sql = """
                SELECT
                    id as n_id,
                FROM nave
                WHERE id = ?
            """;

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
               ps.setInt(1, id);
               try (ResultSet rs = ps.executeQuery()) {
                   if (rs.next()) {
                       Nave nave = new Nave();
                       nave.setId(rs.getInt(1));
                       return nave;
                   } else {
                       throw new SQLException("No se encontró la nave con ID: " + id);
                   }
               } catch (SQLException ex) {
                   throw new SQLException("Error al obtener la nave: " + ex.getMessage());
               }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener la nave: " + ex.getMessage());
        }
    }

    public List<Nave> listarNaves(Integer pageNumber, Integer pageSize) throws SQLException {
        List<Nave> naves = new ArrayList<>();
        String sql = """
                SELECT
                    id,
                    COUNT(*) OVER() AS total_count
                FROM nave
                ORDER BY id
                LIMIT ? OFFSET ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (pageNumber - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Nave nave = new Nave();
                    nave.setId(rs.getInt("id"));
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

    public List<Nave> listarNavesConZonas() throws SQLException {
        List<Nave> naves = new ArrayList<>();
        String sql = """
                SELECT
                    n.id AS n_id,
                    z.id AS z_id,
                    z.tipo AS z_tipo
                FROM nave n
                LEFT JOIN zona z ON z.id_nave = n.id
                ORDER BY n.id, z.id
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Nave currentNave = null;
                while (rs.next()) {
                    int naveId = rs.getInt("n_id");
                    if (currentNave == null || currentNave.getId() != naveId) {
                        currentNave = new Nave();
                        currentNave.setId(naveId);
                        naves.add(currentNave);
                    }

                    int zonaId = rs.getInt("z_id");
                    if (!rs.wasNull()) {
                        Zona zona = new Zona();
                        zona.setId(zonaId);
                        zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo").toUpperCase()));
                        zona.setNave(currentNave);
                        currentNave.agregarZona(zona);
                    }
                }
                return naves;
            } catch (SQLException ex) {
                throw new SQLException("Error al listar las naves con zonas: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las naves con zonas: " + ex.getMessage());
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

    public List<Zona> buscarZonasPorNaveId(Integer naveId, Integer pageNumber, Integer pageSize) throws SQLException {
        String sql = """
                SELECT
                    z.id as z_id,
                    z.tipo as z_tipo_zona,
                    n.id as n_id
                FROM zona z
                INNER JOIN nave n ON z.id_nave = n.id
                WHERE n.id = ?
                ORDER BY z.id
                LIMIT ? OFFSET ?
                """;

        List<Zona> zonas = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, naveId);
                stmt.setInt(2, pageSize);
                stmt.setInt(3, (pageNumber - 1) * pageSize);
               try (ResultSet rs = stmt.executeQuery()) {
                   while (rs.next()) {
                       Zona zona = new Zona();
                       zona.setId(rs.getInt("z_id"));
                       zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo_zona").toUpperCase()));

                       Nave nave = new Nave();
                       nave.setId(rs.getInt("n_id"));
                       zona.setNave(nave);
                       nave.agregarZona(zona);

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

    public List<Zona> buscarZonas(Integer pageNumber, Integer pageSize) throws SQLException {
        String sql = """
                SELECT
                    z.id as z_id,
                    z.tipo as z_tipo_zona,
                    n.id as n_id
                FROM zona z
                INNER JOIN nave n ON z.id_nave = n.id
                ORDER BY z.id
                LIMIT ? OFFSET ?
                """;

        List<Zona> zonas = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, pageSize);
                stmt.setInt(2, (pageNumber - 1) * pageSize);
               try (ResultSet rs = stmt.executeQuery()) {
                   while (rs.next()) {
                       Zona zona = new Zona();
                       zona.setId(rs.getInt("z_id"));
                       zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo_zona").toUpperCase()));

                       Nave nave = new Nave();
                       nave.setId(rs.getInt("n_id"));
                       zona.setNave(nave);
                       nave.agregarZona(zona);

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

    public Ubicacion buscarUbicacionPorId(int id) throws SQLException {
        String sql = """
            SELECT 
                u.id AS u_id,
                u.nro_estanteria AS u_nro_estanteria,
                u.nro_nivel AS u_nro_nivel,
                u.capacidad_usada AS u_capacidad_usada,
                z.id AS z_id,
                z.tipo AS z_tipo
            FROM ubicacion u
            INNER JOIN zona z ON u.id_zona = z.id
            WHERE u.id = ?""";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ubicacion ubicacion = new Ubicacion();
                    ubicacion.setId(rs.getInt("u_id"));
                    ubicacion.setNroEstanteria(rs.getInt("u_nro_estanteria"));
                    ubicacion.setNroNivel(rs.getInt("u_nro_nivel"));
                    ubicacion.setCapacidadUsada(rs.getDouble("u_capacidad_usada"));

                    Zona zona = new Zona();
                    zona.setId(rs.getInt("z_id"));
                    zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo").toUpperCase()));
                    ubicacion.setZona(zona);

                    return ubicacion;
                } else {
                    throw new SQLException("No se encontró la ubicación con ID: " + id);
                }
            } catch (SQLException ex) {
                throw new SQLException("Error al obtener la ubicación: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener la ubicación: " + ex.getMessage());
        }
    }

    public List<Ubicacion> buscarUbicacionesPorZona(Integer zonaId) throws SQLException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = """
            SELECT 
                u.id AS u_id,
                u.nro_estanteria AS u_nro_estanteria,
                u.nro_nivel AS u_nro_nivel,
                u.capacidad_usada AS u_capacidad_usada,
                z.id AS z_id,
                z.tipo AS z_tipo
            FROM ubicacion u
            INNER JOIN zona z ON u.id_zona = z.id
            WHERE z.id = ?
            ORDER BY u.id
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, zonaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setId(rs.getInt("u_id"));
                ubicacion.setNroEstanteria(rs.getInt("u_nro_estanteria"));
                ubicacion.setNroNivel(rs.getInt("u_nro_nivel"));
                ubicacion.setCapacidadUsada(rs.getDouble("u_capacidad_usada"));

                Zona zona = new Zona();
                zona.setId(rs.getInt("z_id"));
                zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo").toUpperCase()));
                ubicacion.setZona(zona);

                ubicaciones.add(ubicacion);
            }
            return ubicaciones;
        } catch (SQLException ex) {
            String error = "Error al listar las ubicaciones por zona: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public List<Ubicacion> listarTodasUbicaciones() throws SQLException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = """
            SELECT 
                u.id AS u_id,
                u.nro_estanteria AS u_nro_estanteria,
                u.nro_nivel AS u_nro_nivel,
                u.capacidad_usada AS u_capacidad_usada,
                z.id AS z_id,
                z.tipo AS z_tipo,
                n.id as n_id
            FROM ubicacion u
            INNER JOIN zona z ON u.id_zona = z.id
            INNER JOIN nave n ON z.id_nave = n.id
            ORDER BY u.id
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setId(rs.getInt("u_id"));
                ubicacion.setNroEstanteria(rs.getInt("u_nro_estanteria"));
                ubicacion.setNroNivel(rs.getInt("u_nro_nivel"));
                ubicacion.setCapacidadUsada(rs.getDouble("u_capacidad_usada"));

                Zona zona = new Zona();
                zona.setId(rs.getInt("z_id"));
                zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo").toUpperCase()));
                ubicacion.setZona(zona);

                Nave nave = new Nave();
                nave.setId(rs.getInt("n_id"));
                zona.setNave(nave);

                ubicaciones.add(ubicacion);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las ubicaciones: " + ex.getMessage());
        }
        return ubicaciones;
    }

    public List<Ubicacion> listarUbicacionesPorProducto(Integer productoId) throws SQLException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = """
            SELECT 
                u.id AS u_id,
                u.nro_estanteria AS u_nro_estanteria,
                u.nro_nivel AS u_nro_nivel,
                u.capacidad_usada AS u_capacidad_usada,
                z.id AS z_id,
                z.tipo AS z_tipo,
                n.id as n_id
            FROM ubicacion u
            inner join producto_ubicacion pu on u.id = pu.id_ubicacion
            Where pu.id_producto = ?
        """;

        try(PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setId(rs.getInt("u_id"));
                ubicacion.setNroEstanteria(rs.getInt("u_nro_estanteria"));
                ubicacion.setNroNivel(rs.getInt("u_nro_nivel"));
                ubicacion.setCapacidadUsada(rs.getDouble("u_capacidad_usada"));

                Zona zona = new Zona();
                zona.setId(rs.getInt("z_id"));
                zona.setTipo(TipoZona.valueOf(rs.getString("z_tipo").toUpperCase()));
                ubicacion.setZona(zona);

                Nave nave = new Nave();
                nave.setId(rs.getInt("n_id"));
                zona.setNave(nave);

                ubicaciones.add(ubicacion);
            }

            return ubicaciones;
        } catch (SQLException ex) {
            throw new SQLException("Error al listar las ubicaciones: " + ex.getMessage());
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

    public void insertarProductoEnUbicacion(Producto producto, Ubicacion ubicacion, double cantidad, boolean esSalida) throws SQLException {
        try {
            connection.setAutoCommit(false);

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
}
