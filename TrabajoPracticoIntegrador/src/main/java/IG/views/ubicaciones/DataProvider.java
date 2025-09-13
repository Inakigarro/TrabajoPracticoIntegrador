package IG.views.ubicaciones;

import IG.domain.Clases.Nave;
import IG.domain.Clases.Ubicacion;
import IG.domain.Clases.Zona;

import java.util.List;

public interface DataProvider {
    List<Nave> listNaves();
    List<Zona> listZonas();
    List<Zona> listZonasPorNave(Nave nave);
    Nave saveNave(Nave n);
    Zona saveZona(Zona z);
    Ubicacion saveUbicacion(Ubicacion u);
}
