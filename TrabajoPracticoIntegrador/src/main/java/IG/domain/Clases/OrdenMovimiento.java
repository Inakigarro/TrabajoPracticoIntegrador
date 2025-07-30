package main.java.IG.domain.Clases;

import main.java.IG.domain.Enums.TipoMovimiento;
import main.java.IG.domain.Enums.OrdenMovimientoEstados;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

 public class OrdenMovimiento {
    private Integer id;
    private TipoMovimiento tipo;
    private LocalDateTime fecha;
    private OrdenMovimientoEstados estado;
    private List<DetalleMovimiento> detalleMovimientoList;

    public OrdenMovimiento() {
        id = 0;
        tipo = TipoMovimiento.SINDEFINIR;
        fecha = LocalDateTime.now();
        estado = OrdenMovimientoEstados.PENDIENTE;
        detalleMovimientoList = new ArrayList<>();
    }

    public OrdenMovimiento(Integer id, TipoMovimiento tipo, LocalDateTime fecha, OrdenMovimientoEstados estado) {
        this();
        this.setId(id);
        this.setTipo(tipo);
        this.setFecha(fecha);
        this.setEstado(estado);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
         if (id == null || id < 1)
            throw new IllegalArgumentException("El id no puede ser nulo.");

         this.id = id;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        if (tipo == null || tipo == TipoMovimiento.SINDEFINIR)
            throw new IllegalArgumentException("El tipo no puede ser nulo");

        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null)
            throw new IllegalArgumentException("La fecha no puede ser nula");

     this.fecha = fecha;
    }

    public OrdenMovimientoEstados getEstado() {
        return estado;
    }

    public void setEstado(OrdenMovimientoEstados estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        this.estado = estado;
    }

    public void agregarDetalle(DetalleMovimiento detalle){
        detalleMovimientoList.add(detalle);
    }

    public void aprobar() {
        this.setEstado(OrdenMovimientoEstados.APROBADO);
    }

     public void ejecutar() {
         if (!estado.equals(OrdenMovimientoEstados.APROBADO))
             throw new IllegalStateException("Solo se puede ejecutar una orden con estado 'aprobado'.");

         if (detalleMovimientoList == null || detalleMovimientoList.isEmpty())
             throw new IllegalStateException("La orden no tiene detalles para ejecutar.");

         for (DetalleMovimiento detalle : detalleMovimientoList) {
             Producto producto = detalle.getProducto();
             double cantidad = detalle.getCantidad();

             if (producto == null)
                 throw new IllegalStateException("Uno de los detalles no tiene producto.");

             if (producto.getStock() <= 0d)
                    throw new IllegalStateException("El producto no tiene stock.");

             if (cantidad <= 0) {
                 throw new IllegalStateException("Cantidad inválida en un detalle.");
             }

             Double stockActual = producto.getStock();  // necesitas que haya getStock()

             if (detalle.esSalida()) {
                 if (cantidad > stockActual) {
                     throw new IllegalStateException("Stock insuficiente para el producto ID: " + producto.getId());
                 }
                 producto.setStock(stockActual - cantidad); // salida: descontar stock
             } else {
                 producto.setStock(stockActual + cantidad); // entrada: aumentar stock
             }
         }

         this.setEstado(OrdenMovimientoEstados.PROCESO);
     }

    @Override
    public String toString() {
        return String.format("%1$d - %2$s - %3$s - %4$s - %5$s",
                this.id, this.tipo, this.fecha, this.estado, this.detalleMovimientoList);
    }
 }