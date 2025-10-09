package IG.domain.Clases;

import IG.application.ServicioProductoUbicacion;
import IG.application.interfaces.IServicioProductoUbicacion;
import IG.domain.Enums.EstadosOrdenes;

import java.util.ArrayList;
import java.util.List;
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
    public void ejecutar(OrdenMovimiento orden) throws Exception {
        IServicioProductoUbicacion servicioProductoUbicacion = new ServicioProductoUbicacion();
        List<Producto> productosAModificar = new ArrayList<>();
        List<Ubicacion> ubicacionesAModificar = new ArrayList<>();
        List<ProductoUbicacion> productosUbicacionAModificar = new ArrayList<>();
        List<ProductoUbicacion> productoUbicacionACrear = new ArrayList<>();

        switch (orden.getTipo()) {
            case INGRESO: {
                for (DetalleMovimiento detalle : orden.getDetalleMovimientoList()) {
                    Ubicacion ubicacion = detalle.getUbicacion();
                    Producto producto = detalle.getProducto();
                    Double cantidad = detalle.getCantidad();
                    if (!detalle.esSalida() && cantidad <= ubicacion.getCapacidadDisponible()) {
                        ProductoUbicacion prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                        if (prodUbi != null && prodUbi.getId() != 0) {
                            prodUbi.setStock(prodUbi.getStock() + cantidad);
                            producto.setStock(producto.getStock() + cantidad);
                            ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() + cantidad);
                            productosAModificar.add(producto);
                            ubicacionesAModificar.add(ubicacion);
                            productosUbicacionAModificar.add(prodUbi);
                        } else {
                            prodUbi = new ProductoUbicacion(producto, ubicacion, cantidad);
                            ubicacion.addProducto(prodUbi);
                            producto.setStock(producto.getStock() + cantidad);
                            ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() + cantidad);
                            productosAModificar.add(producto);
                            ubicacionesAModificar.add(ubicacion);
                            productoUbicacionACrear.add(prodUbi);
                        }
                    } else {
                        logger.warning("El detalle indica una salida en una orden de ingreso. Se omite este detalle.");
                    }
                }
                servicioProductoUbicacion.ingresarEnTransaccion(
                        productosAModificar,
                        ubicacionesAModificar,
                        productosUbicacionAModificar,
                        productoUbicacionACrear);
                break;
            }
            case EGRESO: {
                for (DetalleMovimiento detalle : orden.getDetalleMovimientoList()) {
                    Ubicacion ubicacion = detalle.getUbicacion();
                    Producto producto = detalle.getProducto();
                    Double cantidad = detalle.getCantidad();
                    if (detalle.esSalida() && cantidad > 0 && cantidad <= ubicacion.getCapacidadDisponible()) {
                        if (ubicacion.tieneProducto(producto)) {
                            ProductoUbicacion prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                            if (prodUbi != null && prodUbi.getStock() >= cantidad) {
                                prodUbi.setStock(prodUbi.getStock() - cantidad);
                                ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() - cantidad);
                                producto.setStock(producto.getStock() - cantidad);
                                productosAModificar.add(producto);
                                ubicacionesAModificar.add(ubicacion);
                                productosUbicacionAModificar.add(prodUbi);
                            } else {
                                throw new IllegalStateException("No hay suficiente stock del producto " + producto.getId() + " en la ubicación " + ubicacion.getId());
                            }
                        }
                    } else {
                        logger.warning("El detalle indica un ingreso en una orden de egreso. Se omite este detalle.");
                    }
                }
                servicioProductoUbicacion.egresarEnTransaccion(productosAModificar, ubicacionesAModificar, productosUbicacionAModificar);
                break;
            }
            case INTERNO: {
                for (DetalleMovimiento detalle : orden.getDetalleMovimientoList()) {
                    Ubicacion ubicacion = detalle.getUbicacion();
                    Producto producto = detalle.getProducto();
                    Double cantidad = detalle.getCantidad();
                    if (detalle.esSalida()) {
                        if (cantidad > 0 && ubicacion.tieneStockDisponible(producto.getId(), cantidad)) {
                            ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() - cantidad);
                            var prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                            if (prodUbi != null) {
                                prodUbi.setStock(prodUbi.getStock() - cantidad);
                                productosUbicacionAModificar.add(prodUbi);
                            }
                            ubicacionesAModificar.add(ubicacion);
                        } else {
                            throw new IllegalStateException("No hay suficiente capacidad en la ubicación " + ubicacion.getId());
                        }
                    } else {
                        if (cantidad > 0 && ubicacion.tieneCapacidadDisponible(cantidad)) {
                            ubicacion.setCapacidadUsada(ubicacion.getCapacidadUsada() + cantidad);
                            var prodUbi = ubicacion.getProductoUbicacionPorProductoId(producto.getId());
                            if (prodUbi != null && prodUbi.getId() >= 1) {
                                prodUbi.setStock(prodUbi.getStock() + cantidad);
                                productosUbicacionAModificar.add(prodUbi);
                            } else {
                                prodUbi = new ProductoUbicacion(producto, ubicacion, cantidad);
                                ubicacion.addProducto(prodUbi);
                                productoUbicacionACrear.add(prodUbi);
                            }
                            ubicacionesAModificar.add(ubicacion);
                        } else {
                            throw new IllegalStateException("No hay suficiente stock del producto " + producto.getId() + " en la ubicación " + ubicacion.getId());
                        }
                    }
                }
                servicioProductoUbicacion.internoEnTransaccion(ubicacionesAModificar, productosUbicacionAModificar, productoUbicacionACrear);
                break;
            }
            default: {
                throw new IllegalStateException("Tipo de orden no reconocido.");
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

