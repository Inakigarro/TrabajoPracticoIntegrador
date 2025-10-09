package IG.application;

import IG.application.Dtos.Ubicacion.NaveDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.application.Dtos.Ubicacion.ZonaDto;
import IG.application.interfaces.IServicioUbicaciones;
import IG.config.ConexionBD;
import IG.domain.Clases.*;
import IG.domain.DAO.ProductoUbicacionDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioUbicaciones implements IServicioUbicaciones {
    public ServicioUbicaciones() {}

    public NaveDto crearNave() throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            Nave nuevaNave = prodUbiDao.insertarNave(new Nave());
            return new NaveDto(nuevaNave.getId());
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public List<NaveDto> obtenerNaves() {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            List<Nave> naves = prodUbiDao.listarNaves();
            return naves.stream().map(NaveDto::map).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener las naves: " + exception.getMessage();
            System.out.println(error);
            return new ArrayList<>();
        }
    }

    public ZonaDto crearZona(Zona zona) throws Exception{
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            Zona nuevaZona = prodUbiDao.insertarZona(zona);
            return new ZonaDto(
                    nuevaZona.getId(),
                    nuevaZona.getTipo().getDescripcion(),
                    new NaveDto(nuevaZona.getNave().getId())
            );
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public ZonaDto obtenerZonaPorId(Integer idZona) {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            Zona zona = prodUbiDao.buscarZonaPorId(idZona);
            if (zona != null) {
                return new ZonaDto(
                        zona.getId(),
                        zona.getTipo().getDescripcion(),
                        new NaveDto(zona.getNave().getId())
                );
            } else {
                return null;
            }
        } catch (SQLException exception) {
            String error = "Error al obtener la zona: " + exception.getMessage();
            System.out.println(error);
            return null;
        }
    }

    public List<ZonaDto> obtenerZonas(Integer idNave) {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            List<Zona> zonas = prodUbiDao.buscarZonasPorNaveId(idNave);
            return zonas.stream().map(ZonaDto::map).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener las zonas: " + exception.getMessage();
            System.out.println(error);
            return new ArrayList<>();
        }
    }

    public UbicacionDto crearUbicacion(Ubicacion ubicacion) throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            Ubicacion nuevaUbi = prodUbiDao.insertarUbicacion(ubicacion);
            return new UbicacionDto(
                    nuevaUbi.getId(),
                    nuevaUbi.getNroEstanteria(),
                    nuevaUbi.getNroNivel(),
                    nuevaUbi.getCapacidadUsada(),
                    ZonaDto.map(nuevaUbi.getZona()),
                    new HashMap<>()
            );
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public List<UbicacionDto> listarUbicaciones() {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO dao = new ProductoUbicacionDAO(conn);
            return dao.listarTodasUbicaciones().stream().map(UbicacionDto::map).toList();
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    public List<UbicacionDto> listarUbicacionesPorProducto(Integer idProducto) {
        try(var conn =  IG.config.ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO dao = new ProductoUbicacionDAO(conn);
            return dao.listarUbicacionesPorProducto(idProducto).stream().map(UbicacionDto::map).toList();
        }  catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    public List<UbicacionDto> obtenerTodasLasUbicaciones() {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            List<Ubicacion> ubicaciones = prodUbiDao.listarTodasUbicaciones();
            ubicaciones.forEach(u -> {
                try {
                    u.addRangeProductos(prodUbiDao.listarProductosPorUbicacion(u.getId()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            return ubicaciones.stream().map(UbicacionDto::map).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener las ubicaciones: " + exception.getMessage();
            System.out.println(error);
            return new ArrayList<>();
        }
    }

    public void actualizarUbicacion(Ubicacion ubicacion) throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            prodUbiDao.actualizarUbicacion(ubicacion);
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public void insertarProductoUbicacion(ProductoUbicacion productoUbicacion) throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            prodUbiDao.insertarProductoEnUbicacion(productoUbicacion);
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public void actualizarProductoUbicacion(ProductoUbicacion productoUbicacion) throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            prodUbiDao.actualizarProductoUbicacion(productoUbicacion);
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public void actualizarEnTransaccion(List<Ubicacion> ubicaciones, List<ProductoUbicacion> productoUbicacionesAModificar, List<ProductoUbicacion> productoUbicacionesACrear) throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            try {
                conn.setAutoCommit(false);
                ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
                for (Ubicacion u : ubicaciones) {
                    prodUbiDao.actualizarUbicacion(u);
                }
                for (ProductoUbicacion pu : productoUbicacionesAModificar) {
                    prodUbiDao.actualizarProductoUbicacion(pu);
                }
                for (ProductoUbicacion pu : productoUbicacionesACrear) {
                    prodUbiDao.insertarProductoEnUbicacion(pu);
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                String error = "Error al intentar actualizar en transacción: ";
                System.out.println(error);
                throw new SQLException(error + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }
}
