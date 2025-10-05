package IG.application.interfaces;

import IG.application.Dtos.Ubicacion.NaveDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.application.Dtos.Ubicacion.ZonaDto;
import IG.domain.Clases.Producto;
import IG.domain.Clases.Ubicacion;
import IG.domain.Clases.Zona;

import java.sql.SQLException;
import java.util.List;

public interface IServicioUbicaciones {
    // Naves.
    public NaveDto crearNave() throws Exception;
    public List<NaveDto> obtenerNaves();
    // Zonas.
    public ZonaDto crearZona(Zona zona) throws Exception;
    public List<ZonaDto> obtenerZonas(Integer idNave);
    public ZonaDto obtenerZonaPorId(Integer idZona);
    // Ubicaciones.
    public UbicacionDto crearUbicacion(Ubicacion ubicacion) throws Exception;
    public List<UbicacionDto> obtenerUbicacionesPorZonaId(Integer idZona) throws Exception;
    public List<UbicacionDto> obtenerTodasLasUbicaciones() throws SQLException;
    public void insertarProductoUbicacion(Producto producto, Ubicacion ubicacion, Double cantidad, Boolean esSalida) throws Exception;
}
