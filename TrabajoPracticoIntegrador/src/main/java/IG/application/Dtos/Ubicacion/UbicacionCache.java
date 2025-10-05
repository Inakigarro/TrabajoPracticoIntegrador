package IG.application.Dtos.Ubicacion;

import IG.application.Dtos.ProductoUbicacionCache;

import java.util.ArrayList;
import java.util.List;

public class UbicacionCache {
    public Integer id;
    public Integer nroEstanteria;
    public Integer nroNivel;
    public Double capacidadUsada;
    public ZonaDto zona;
    public List<ProductoUbicacionCache> productos;

    public UbicacionCache(Integer id, Integer nroEstanteria, Integer nroNivel, Double capacidadUsada, ZonaDto zona) {
        this.id = id;
        this.nroEstanteria = nroEstanteria;
        this.nroNivel = nroNivel;
        this.capacidadUsada = capacidadUsada;
        this.zona = zona;
        this.productos = new ArrayList<>();
    }

    public static UbicacionCache map(UbicacionDto ubicacion) {
        var cache = new UbicacionCache(
            ubicacion.id(),
            ubicacion.nroEstanteria(),
            ubicacion.nroNivel(),
            ubicacion.capacidadUsada(),
            ubicacion.zona()
        );

        if (ubicacion.productos() != null && !ubicacion.productos().isEmpty()) {
            cache.productos = ubicacion.productos()
                    .stream().map(ProductoUbicacionCache::map).toList();
        }

        return cache;
    }
}
