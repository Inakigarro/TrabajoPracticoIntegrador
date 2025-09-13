package IG.application;

import IG.application.Dtos.NaveDto;
import IG.application.Dtos.UbicacionDto;
import IG.application.Dtos.ZonaDto;
import IG.application.interfaces.IServicioUbicaciones;
import IG.config.ConexionBD;
import IG.domain.Clases.Nave;
import IG.domain.Clases.Ubicacion;
import IG.domain.Clases.Zona;
import IG.domain.DAO.ProductoUbicacionDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            List<Nave> naves = prodUbiDao.listarNaves(1, 100);
            return naves.stream().map(n -> new NaveDto(n.getId())).toList();
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

    public List<Zona> obtenerZonas() {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            return prodUbiDao.buscarZonas(1, 100);
        } catch (SQLException exception) {
            String error = "Error al obtener las zonas: " + exception.getMessage();
            System.out.println(error);
            return new ArrayList<>();
        }
    }

    public List<ZonaDto> obtenerZonas(Integer idNave) {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            List<Zona> zonas = prodUbiDao.buscarZonasPorNaveId(idNave, 1, 100);
            return zonas.stream().map(z -> new ZonaDto(
                    z.getId(),
                    z.getTipo().getDescripcion(),
                    new NaveDto(z.getNave().getId())
                    )).toList();
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
                    new ZonaDto(
                            nuevaUbi.getZona().getId(),
                            nuevaUbi.getZona().getTipo().getDescripcion(),
                            new NaveDto(nuevaUbi.getZona().getNave().getId())
                    )
            );
        } catch (SQLException ex) {
            String error = "Error al intentar establecer conexion con la base de datos: ";
            System.out.println(error);
            throw new SQLException(error + ex.getMessage());
        }
    }

    public List<UbicacionDto> obtenerUbicacionesPorZonaId(Integer idZona) throws Exception {
        try(var conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO prodUbiDao = new ProductoUbicacionDAO(conn);
            List<Ubicacion> ubicaciones = prodUbiDao.buscarUbicacionesPorZona(idZona);
            return ubicaciones.stream().map(u -> new UbicacionDto(
                    u.getId(),
                    u.getNroEstanteria(),
                    u.getNroNivel(),
                    new ZonaDto(
                            u.getZona().getId(),
                            u.getZona().getTipo().getDescripcion(),
                            new NaveDto(u.getZona().getNave().getId())
                    )
            )).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener las ubicaciones: " + exception.getMessage();
            System.out.println(error);
            throw new Exception(error, exception);
        }
    }
}
