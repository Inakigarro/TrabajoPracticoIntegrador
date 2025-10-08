package IG.domain.DAO;

import IG.domain.Clases.*;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.ProductoUnidades;
import IG.domain.Enums.TipoMovimiento;
import IG.domain.Enums.TipoZona;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper {
    public static TipoProducto mapRowToTipoProducto(ResultSet rs) throws SQLException {
        TipoProducto tp = new TipoProducto();
        tp.setId(rs.getInt("tp_id"));
        tp.setDescripcion(rs.getString("tp_descripcion"));
        return tp;
    }

    public static Producto mapRowToProducto(ResultSet rs) throws SQLException {
        TipoProducto tp = mapRowToTipoProducto(rs);

        Producto p = new Producto();
        p.setId(rs.getInt("p_id"));
        p.setDescripcion(rs.getString("p_descripcion"));
        p.setCantidadUnidad(rs.getDouble("p_cantidad_unidad"));
        p.setUnidadMedida(ProductoUnidades.fromDescription(rs.getString("p_unidad_medida")));
        p.setStock(rs.getDouble("p_stock"));

        p.setTipoProducto(tp);
        return p;
    }

    public static Nave mapRowToNave(ResultSet rs) throws SQLException {
        Nave n = new Nave();
        n.setId(rs.getInt("n_id"));
        return n;
    }

    public static Zona mapRowToZona(ResultSet rs) throws SQLException {
        Nave n = mapRowToNave(rs);

        Zona z = new Zona();
        z.setId(rs.getInt("z_id"));
        z.setTipo(TipoZona.fromDescription(rs.getString("z_tipo")));
        z.setNave(n);

        return z;
    }

    public static Ubicacion mapRowToUbicacion(ResultSet rs) throws SQLException {
        Nave n = mapRowToNave(rs);

        Zona z = mapRowToZona(rs);

        Ubicacion u = new Ubicacion();
        u.setId(rs.getInt("u_id"));
        u.setNroEstanteria(rs.getInt("u_nro_estanteria"));
        u.setNroNivel(rs.getInt("u_nro_nivel"));
        u.setCapacidadUsada(rs.getDouble("u_capacidad_usada"));
        u.setZona(z);

        return u;
    }

    public static Ubicacion mapRowToUbicacionCompleto(ResultSet rs) throws SQLException {
        Nave n = mapRowToNave(rs);

        Zona z = mapRowToZona(rs);

        TipoProducto tp = mapRowToTipoProducto(rs);

        Producto p = mapRowToProducto(rs);

        Ubicacion u = new Ubicacion();
        u.setId(rs.getInt("u_id"));
        u.setNroEstanteria(rs.getInt("u_nro_estanteria"));
        u.setNroNivel(rs.getInt("u_nro_nivel"));
        u.setCapacidadUsada(rs.getDouble("u_capacidad_usada"));
        u.setZona(z);

        ProductoUbicacion pu = mapRowToProductoUbicacionCompleto(rs);
        u.addProducto(pu);
        return u;
    }

    public static ProductoUbicacion mapRowToProductoUbicacionCompleto(ResultSet rs) throws SQLException {
        Producto p = mapRowToProducto(rs);
        Ubicacion u = mapRowToUbicacion(rs);

        ProductoUbicacion pu = new ProductoUbicacion();
        pu.setProducto(p);
        pu.setUbicacion(u);
        pu.setId(rs.getInt("pu_id"));
        pu.setStock(rs.getDouble("pu_stockProductoUbicacion"));

        return pu;
    }

    public static ProductoUbicacion mapRowToProductoUbicacionSinProducto(ResultSet rs) throws SQLException {
        Ubicacion u = mapRowToUbicacion(rs);

        ProductoUbicacion pu = new ProductoUbicacion();
        pu.setId(rs.getInt("pu_id"));
        pu.setUbicacion(u);
        pu.setStock(rs.getDouble("pu_stockProductoUbicacion"));

        return pu;
    }

    public static ProductoUbicacion mapRowToProductoUbicacionSinUbicacion(ResultSet rs) throws SQLException {
        Producto p = mapRowToProducto(rs);

        ProductoUbicacion pu = new ProductoUbicacion();
        pu.setId(rs.getInt("pu_id"));
        pu.setProducto(p);
        pu.setStock(rs.getDouble("pu_stockProductoUbicacion"));

        return pu;
    }

    public static OrdenMovimiento mapRowToOrdenMovimiento(ResultSet rs) throws SQLException {
        OrdenMovimiento om = new OrdenMovimiento();
        om.setId(rs.getInt("om_id"));
        om.setTipo(TipoMovimiento.fromDescripcion(rs.getString("om_tipo")));
        om.setFecha(rs.getTimestamp("om_fecha").toLocalDateTime());
        String estadoStr = rs.getString("om_estado");
        switch (estadoStr) {
            case "Pendiente" -> {
                om.setEstadoOrden(new PendienteEstado());
                om.setEstado(EstadosOrdenes.PENDIENTE);
            }
            case "En Proceso" -> {
                om.setEstadoOrden(new EnProcesoEstado());
                om.setEstado(EstadosOrdenes.PROCESO);
            }
            case "Completado" -> {
                om.setEstadoOrden(new CompletadaEstado());
                om.setEstado(EstadosOrdenes.COMPLETADO);
            }
            case "Cancelado" -> {
                om.setEstadoOrden(new CanceladaEstado());
                om.setEstado(EstadosOrdenes.CANCELADO);
            }
            default -> throw new SQLException("Estado desconocido: " + estadoStr);
        }
        return om;
    }

    public static DetalleMovimiento mapRowToDetalleMovimiento(ResultSet rs) throws SQLException {
        Producto p = mapRowToProducto(rs);
        Ubicacion u = mapRowToUbicacionCompleto(rs);
        DetalleMovimiento dm = new DetalleMovimiento();
        dm.setId(rs.getInt("dm_id"));
        dm.setCantidad(rs.getDouble("dm_cantidad"));
        dm.setEsSalida(rs.getBoolean("dm_es_salida"));
        dm.setProducto(p);
        dm.setUbicacion(u);
        return dm;
    }
}
