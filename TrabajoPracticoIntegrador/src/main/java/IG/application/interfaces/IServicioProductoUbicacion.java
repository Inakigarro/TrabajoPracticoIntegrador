package IG.application.interfaces;

import IG.domain.Clases.Producto;
import IG.domain.Clases.ProductoUbicacion;
import IG.domain.Clases.Ubicacion;

import java.util.List;

public interface IServicioProductoUbicacion {
    void ingresarEnTransaccion(
            List<Producto> productosAModificar,
            List<Ubicacion> ubicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesACrear
    ) throws Exception;
    void egresarEnTransaccion(
            List<Producto> productosAModificar,
            List<Ubicacion> ubicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesAModificar
    ) throws Exception;
    void internoEnTransaccion(
            List<Ubicacion> ubicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesAModificar,
            List<ProductoUbicacion> productoUbicacionesACrear
    ) throws Exception;
}
