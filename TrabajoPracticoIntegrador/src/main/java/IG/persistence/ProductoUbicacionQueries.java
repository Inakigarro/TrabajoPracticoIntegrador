package IG.persistence;

public class ProductoUbicacionQueries {
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
}
