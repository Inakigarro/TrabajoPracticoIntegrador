package IG.application.Dtos;

import IG.application.Dtos.Producto.ProductoCache;
import IG.application.Dtos.Ubicacion.UbicacionCache;

public class ProductoUbicacionCache {
    public Integer id;
    public ProductoCache producto;
    public UbicacionCache ubicacion;
    public Double stockProductoUbicacion;

    public ProductoUbicacionCache(Integer id, ProductoCache producto, UbicacionCache ubicacion, Double stockProductoUbicacion) {
        this.id = id;
        this.producto = producto;
        this.ubicacion = ubicacion;
        this.stockProductoUbicacion = stockProductoUbicacion;
    }

    public static ProductoUbicacionCache map(ProductoUbicacionDto prodUbi) {
        return new ProductoUbicacionCache(
                prodUbi.id(),
                ProductoCache.map(prodUbi.producto()),
                UbicacionCache.map(prodUbi.ubicacion()),
                prodUbi.stockProductoUbicacion()
        );
    }
}
