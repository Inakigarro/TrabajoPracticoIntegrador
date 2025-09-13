package IG.application.interfaces;

import IG.application.Dtos.NaveDto;
import IG.application.Dtos.UbicacionDto;
import IG.application.Dtos.ZonaDto;
import IG.domain.Clases.Ubicacion;
import IG.domain.Clases.Zona;

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
}
