package IG.application;

import IG.application.interfaces.IServicioProductoUbicacion;
import IG.config.ConexionBD;
import IG.domain.Clases.Producto;
import IG.domain.Clases.ProductoUbicacion;
import IG.domain.Clases.Ubicacion;
import IG.domain.DAO.ProductoUbicacionDAO;

import java.util.List;

public class ServicioProductoUbicacion implements IServicioProductoUbicacion {
    @Override
    public void ingresarEnTransaccion(
            List<Producto> productosAModificar,
            List<Ubicacion> ubicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesACrear
    ) throws Exception {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            conn.setAutoCommit(false);
            try {
                for (Producto p : productosAModificar) {
                    prodUbiDao.actualizarProducto(p);
                }
                for (Ubicacion u : ubicacionesAModificar) {
                    prodUbiDao.actualizarUbicacion(u);
                }
                for (ProductoUbicacion pu : productoUbicacionesAModificar) {
                    prodUbiDao.actualizarProductoUbicacion(pu);
                }
                for (ProductoUbicacion pu : productoUbicacionesACrear) {
                    prodUbiDao.insertarProductoEnUbicacion(pu);
                }
            } catch (Exception ex) {
                conn.rollback();
                String error = "Error al intentar actualizar en transacción: ";
                System.out.println(error);
                throw new Exception(error + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void egresarEnTransaccion(
            List<Producto> productosAModificar,
            List<Ubicacion> ubicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesAModificar
    ) throws Exception {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            conn.setAutoCommit(false);
            try {
                for (Producto p : productosAModificar) {
                    prodUbiDao.actualizarProducto(p);
                }
                for (Ubicacion u : ubicacionesAModificar) {
                    prodUbiDao.actualizarUbicacion(u);
                }
                for (ProductoUbicacion pu : productoUbicacionesAModificar) {
                    prodUbiDao.actualizarProductoUbicacion(pu);
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                String error = "Error al intentar egresar en transacción: ";
                System.out.println(error);
                throw new Exception(error + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void internoEnTransaccion(
            List<Ubicacion> ubicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesACrear
    ) throws Exception {
        try (var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            conn.setAutoCommit(false);
            try {
                for (Ubicacion u : ubicacionesAModificar) {
                    prodUbiDao.actualizarUbicacion(u);
                }
                for (ProductoUbicacion pu : productoUbicacionesAModificar) {
                    prodUbiDao.actualizarProductoUbicacion(pu);
                }
                for (ProductoUbicacion pu : productoUbicacionesACrear) {
                    prodUbiDao.insertarProductoEnUbicacion(pu);
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                String error = "Error al intentar realizar movimiento interno en transacción: ";
                System.out.println(error);
                throw new Exception(error + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
