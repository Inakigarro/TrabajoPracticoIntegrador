package IG.domain.Clases;

import IG.application.ServicioProductosDao;
import IG.application.ServicioUbicaciones;
import IG.application.interfaces.IServicioProductos;
import IG.application.interfaces.IServicioUbicaciones;
import IG.domain.Constants.UbicacionConstants;
import IG.domain.Enums.EstadosOrdenes;

import java.util.logging.Logger;

public class EnProcesoEstado implements EstadoOrdenMovimiento {

    Logger logger = Logger.getLogger(EnProcesoEstado.class.getName());

    @Override
    public EstadosOrdenes getEstado() {
        return EstadosOrdenes.PROCESO;
    }

    @Override
    public void aprobar(OrdenMovimiento orden) {
        throw new IllegalStateException("La orden ya está en proceso. No se puede aprobar nuevamente.");
    }

    @Override
    public void ejecutar(OrdenMovimiento orden) {
        IServicioUbicaciones servicioUbicaciones = new ServicioUbicaciones();
        IServicioProductos servicioProducto = new ServicioProductosDao();
        for (DetalleMovimiento detalle : orden.getDetalleMovimientoList()) {
            Ubicacion ubicacion = detalle.getUbicacion();
            double cantidad = detalle.getCantidad();
            Producto producto = detalle.getProducto();
            double capacidadDisponible = UbicacionConstants.UBICACION_CAPACIDAD_MAX - ubicacion.getCapacidadUsada();
            try {
                switch (orden.getTipo()) {
                    case INGRESO: {
                        if (!detalle.esSalida() && cantidad <= capacidadDisponible) {
                            ProductoUbicacion prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                            if (prodUbi != null && prodUbi.getId() != 0) {
                                prodUbi.setStock(prodUbi.getStock() + cantidad);
                                producto.setStock(producto.getStock() + cantidad);
                                ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() + cantidad);
                                servicioProducto.actualizarProducto(producto);
                                servicioUbicaciones.actualizarUbicacion(ubicacion);
                                servicioUbicaciones.actualizarProductoUbicacion(prodUbi);
                            } else {
                                prodUbi = new ProductoUbicacion(producto, ubicacion, cantidad);
                                ubicacion.addProducto(prodUbi);
                                producto.setStock(producto.getStock() + cantidad);
                                ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() + cantidad);
                                servicioUbicaciones.actualizarUbicacion(ubicacion);
                                servicioProducto.actualizarProducto(producto);
                                servicioUbicaciones.insertarProductoUbicacion(prodUbi);
                            }
                        } else {
                            logger.warning("El detalle indica una salida en una orden de ingreso. Se omite este detalle.");
                        }
                        break;
                    }
                    case EGRESO: {
                        if (detalle.esSalida() && cantidad > 0 && cantidad <= capacidadDisponible) {
                            if (ubicacion.tieneProducto(producto)) {
                                ProductoUbicacion prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                                if (prodUbi != null && prodUbi.getStock() >= cantidad) {
                                    prodUbi.setStock(prodUbi.getStock() - cantidad);
                                    ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() - cantidad);
                                    producto.setStock(producto.getStock() - cantidad);
                                    servicioProducto.actualizarProducto(producto);
                                    servicioUbicaciones.actualizarUbicacion(ubicacion);
                                    servicioUbicaciones.actualizarProductoUbicacion(prodUbi);
                                } else {
                                    throw new IllegalStateException("No hay suficiente stock del producto " + producto.getId() + " en la ubicación " + ubicacion.getId());
                                }
                            }
                        } else {
                            logger.warning("El detalle indica un ingreso en una orden de egreso. Se omite este detalle.");
                        }
                        break;
                    }
                    case INTERNO: {
                        // Si es movimiento interno, no se modifica el stock del producto.
                        // Solo la capacidad usada de la ubicacion y el stock en ProductoUbicacion si aplica.
                        if (detalle.esSalida()) {
                            if (cantidad > 0 && ubicacion.tieneCapacidadDisponible(cantidad)) {
                                ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() - cantidad);
                                var prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                                if (prodUbi != null) {
                                    prodUbi.setStock(prodUbi.getStock() - cantidad);
                                    servicioUbicaciones.actualizarProductoUbicacion(prodUbi);
                                }
                                servicioUbicaciones.actualizarUbicacion(ubicacion);
                            } else {
                                throw new IllegalStateException("No hay suficiente capacidad en la ubicación " + ubicacion.getId());
                            }
                        } else {
                            if (cantidad > 0 && ubicacion.tieneStockDisponible(producto.getId(), cantidad)) {
                                ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() + cantidad);
                                var prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                                if (prodUbi != null) {
                                    prodUbi.setStock(prodUbi.getStock() + cantidad);
                                    servicioUbicaciones.actualizarProductoUbicacion(prodUbi);
                                } else {
                                    prodUbi = new ProductoUbicacion(producto, ubicacion, cantidad);
                                    ubicacion.addProducto(prodUbi);
                                    servicioUbicaciones.insertarProductoUbicacion(prodUbi);
                                }
                                servicioUbicaciones.actualizarUbicacion(ubicacion);
                            } else {
                                throw new IllegalStateException("No hay suficiente stock del producto " + producto.getId() + " en la ubicación " + ubicacion.getId());
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("Error al obtener la ubicación: " + e.getMessage());
            }
        }
        orden.setEstadoOrden(new CompletadaEstado());
        orden.setEstado(EstadosOrdenes.COMPLETADO);
    }

    @Override
    public void completar(OrdenMovimiento orden) {
        throw new IllegalStateException("Debe ejecutar la orden antes de completarla.");
    }

    @Override
    public void cancelar(OrdenMovimiento orden) {
        orden.setEstadoOrden(new CanceladaEstado());
        orden.setEstado(EstadosOrdenes.CANCELADO);
    }
}

