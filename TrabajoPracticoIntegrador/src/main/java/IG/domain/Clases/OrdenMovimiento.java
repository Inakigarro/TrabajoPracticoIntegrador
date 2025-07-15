package main.java.IG.domain.Clases;

import IG.domain.Enums.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



 public class OrdenMovimiento {

    private String id;
    private TipoMovimiento tipo;
    private LocalDateTime fecha;
    private String estado;
    private List<DetalleMovimiento> detalleMovimientoList;

     public OrdenMovimiento() {
         setId("SIN ID");
         setTipo(TipoMovimiento.SINDEFINIR);
         setFecha(LocalDateTime.now());
         setEstado("pendiente");
         setDetalleMovimientoList(new ArrayList<>());
     }


     public OrdenMovimiento(String id, TipoMovimiento tipo, LocalDateTime fecha, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.estado = estado;
        this.detalleMovimientoList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
         if (id == null)
            throw new IllegalArgumentException("El id no puede ser nulo.");
        }
        this.id = id;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser nulo");
        }
         this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
     this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estado = estado;
    }

    public void agregarDetalle(DetalleMovimiento detalle){
    detalleMovimientoList.add(detalle);
    }

 public void aprobar() {
     switch (estado.toLowerCase()) {
         case "pendiente":
             estado = "aprobado";
             break;
         case "aprobado":
             throw new IllegalStateException("La orden ya está aprobada.");
         case "anulado":
             throw new IllegalStateException("No se puede aprobar una orden anulada.");
         default:
             throw new IllegalStateException("Estado no válido: " + estado);
     }
 }

 public void ejecutar() {
     if (estado == null || !estado.equalsIgnoreCase("aprobado")) {
         throw new IllegalStateException("Solo se puede ejecutar una orden con estado 'aprobado'.");
     }

     if (detalleMovimientoList == null || detalleMovimientoList.isEmpty()) {
         throw new IllegalStateException("La orden no tiene detalles para ejecutar.");
     }

     for (DetalleMovimiento detalle : detalleMovimientoList) {
         Producto producto = detalle.getProducto();
         double cantidad = detalle.getCantidad();

         if (producto == null) {
             throw new IllegalStateException("Uno de los detalles no tiene producto.");
         }

         if (cantidad <= 0) {
             throw new IllegalStateException("Cantidad inválida en un detalle.");
         }

         Double stockActual = producto.getStock();  // necesitas que haya getStock()

         if (stockActual == null) {
             throw new IllegalStateException("El producto no tiene stock definido.");
         }

         if (detalle.isEsSalida()) {
             if (cantidad > stockActual) {
                 throw new IllegalStateException("Stock insuficiente para el producto ID: " + producto.getId());
             }
             producto.setStock(stockActual - cantidad); // salida: descontar stock
         } else {
             producto.setStock(stockActual + cantidad); // entrada: aumentar stock
         }
     }

     estado = "ejecutado";
 }