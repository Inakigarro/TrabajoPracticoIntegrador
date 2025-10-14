package IG.application.Dtos;

import IG.application.Dtos.Producto.ProductoCache;
import IG.application.Dtos.Ubicacion.UbicacionDto;

public class ProductoUbicacionCache {
    public Integer id;
    public Double stock;
    public ProductoCache producto;
    public UbicacionDto ubicacion;

    public ProductoUbicacionCache(Integer id, Double stock, ProductoCache producto, UbicacionDto ubicacion) {
        this.id = id;
        this.stock = stock;
        this.producto = producto;
        this.ubicacion = ubicacion;
    }

    // ✅ Método map que la view espera
    public static ProductoUbicacionCache map(ProductoUbicacionDto prodUbi) {
        // Convertir ProductoDto a ProductoCache
        ProductoCache productoCache = ProductoCache.map(prodUbi.producto());

        return new ProductoUbicacionCache(
                prodUbi.id(),
                prodUbi.stock(),
                productoCache,
                prodUbi.ubicacion()
        );
    }

    // ✅ Método map que retorna Double (para casos donde esperan .map que retorne stock)
    public static Double map(ProductoUbicacionCache cache) {
        return cache.stock();
    }

    // ✅ Métodos que la view espera
    public Integer id() {
        return this.id;
    }

    public Double stock() {
        return this.stock;
    }

    public ProductoCache producto() {
        return this.producto;
    }

    public UbicacionDto ubicacion() {
        return this.ubicacion;
    }

    public Double stockProductoUbicacion() {
        return this.stock;
    }
}