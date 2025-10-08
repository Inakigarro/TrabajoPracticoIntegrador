package IG.application.Dtos.Ubicacion;

import java.util.HashMap;
import java.util.Map;

public class UbicacionCache {
    public Integer id;
    public Integer nroEstanteria;
    public Integer nroNivel;
    public Double capacidadUsada;
    public ZonaDto zona;
    public Map<Integer, Double> productos;

    public UbicacionCache(Integer id, Integer nroEstanteria, Integer nroNivel, Double capacidadUsada, ZonaDto zona) {
        this.id = id;
        this.nroEstanteria = nroEstanteria;
        this.nroNivel = nroNivel;
        this.capacidadUsada = capacidadUsada;
        this.zona = zona;
        this.productos = new HashMap<Integer, Double>();
    }

    public static UbicacionCache map(UbicacionDto ubicacion) {
        var cache = new UbicacionCache(
            ubicacion.id(),
            ubicacion.nroEstanteria(),
            ubicacion.nroNivel(),
            ubicacion.capacidadUsada(),
            ubicacion.zona()
        );

        cache.productos = ubicacion.productos();

        return cache;
    }
}
