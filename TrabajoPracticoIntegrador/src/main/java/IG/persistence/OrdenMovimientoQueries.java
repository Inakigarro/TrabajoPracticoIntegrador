package IG.persistence;

public class OrdenMovimientoQueries {
    public static final String listarDestallesPorOrden = """
            SELECT
                dm.id AS dm_id,
                dm.cantidad AS dm_cantidad,
                dm.id_producto AS dm_id_producto,
                dm.id_ubicacion AS dm_id_ubicacion,
                dm.es_salida AS dm_es_salida,
                u.id AS u_id,
                u.nro_estanteria AS u_nro_estanteria,
                u.nro_nivel AS u_nro_nivel,
                u.capacidad_usada AS u_capacidad_usada,
                z.id AS z_id,
                z.tipo AS z_tipo,
                n.id AS n_id,
                p.id AS p_id,
                p.descripcion AS p_descripcion,
                p.cantidad_unidad AS p_cantidad_unidad,
                p.unidad_medida AS p_unidad_medida,
                p.stock AS p_stock,
                tp.id AS tp_id,
                tp.descripcion AS tp_descripcion,
                pu.id AS pu_id,
                pu.stockProductoUbicacion AS pu_stockProductoUbicacion
            FROM detalle_movimiento dm
            INNER JOIN producto p ON dm.id_producto = p.id
            INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id
            INNER JOIN ubicacion u ON dm.id_ubicacion = u.id
            INNER JOIN zona z ON u.id_zona = z.id
            INNER JOIN nave n ON z.id_nave = n.id
            LEFT JOIN producto_ubicacion pu ON pu.id_ubicacion = u.id AND pu.id_producto = p.id
            WHERE dm.id_orden_movimiento = ?;
            """;
}
