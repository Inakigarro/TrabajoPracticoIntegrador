package IG.persistence;

public class ProductoUbicacionQueries {
    public static final String listarNaves = """
            SELECT
                id AS n_id
            FROM nave
            ORDER BY id
            """;

    public static final String listarZonasPorNaveId = """
            SELECT
                z.id as z_id,
                z.tipo as z_tipo_zona,
                n.id as n_id
            FROM zona z
            INNER JOIN nave n ON z.id_nave = n.id
            WHERE n.id = ?
            ORDER BY z.id
            """;
    public static final String listarUbicaciones = """
                SELECT
                	u.id AS u_id,
                	u.nro_estanteria AS u_nro_estanteria,
                	u.nro_nivel AS u_nro_nivel,
                	u.capacidad_usada AS u_capacidad_usada,
                	z.id AS z_id,
                	z.tipo AS z_tipo,
                	n.id AS n_id
                FROM ubicacion u
                INNER JOIN zona z ON u.id_zona = z.id
                INNER JOIN nave n ON z.id_nave = n.id
                ORDER BY u.id
            """;

    public static final String listarUbicacionesProProducto = """
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
            INNER JOIN producto_ubicacion pu on u.id = pu.id_ubicacion
            Where pu.id_producto = ?
            """;

    public static final String listarProductosPorUbicacion = """
                SELECT
                    pu.id AS pu_id,
                    pu.stockProductoUbicacion AS pu_stockProductoUbicacion,
                    p.id AS p_id,
                    p.descripcion AS p_descripcion,
                    p.unidad_medida AS p_unidad_medida,
                    p.cantidad_unidad AS p_cantidad_unidad,
                    p.stock AS p_stock,
                    tp.id AS tp_id,
                    tp.descripcion AS tp_descripcion
                FROM producto_ubicacion pu
                INNER JOIN producto p ON pu.id_producto = p.id
                INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id
                WHERE pu.id_ubicacion =  ?
            """;

    public static final String listarProductos = """
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
}
