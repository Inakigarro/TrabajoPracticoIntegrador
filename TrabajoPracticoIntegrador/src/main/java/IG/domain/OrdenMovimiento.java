package main.java.IG.domain;

import main.java.IG.domain.Constants.OrdenMovimientoConstants;
import main.java.IG.domain.Enums.OrdenMovimientoEstados;
import main.java.IG.domain.Enums.TipoMovimiento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrdenMovimiento {
    private Integer id;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private OrdenMovimientoEstados estado;
    private List<DetalleMovimiento> detalles;

    public OrdenMovimiento() {
        this.id = 0;
        this.tipoMovimiento = TipoMovimiento.SINDEFINIR;
        this.estado = OrdenMovimientoEstados.PENDIENTE;
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }

    public void setId(Integer id) {
        if (id == null || id < OrdenMovimientoConstants.ORDEN_MOVIMIENTO_ID_MIN) {
            throw new IllegalArgumentException("El id no puede ser nulo y debe ser mayor o igual a uno.");
        }

        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }

        if (fecha.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual.");
        }

        this.fecha = fecha;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern(OrdenMovimientoConstants.ORDEN_MOVIMIENTO_FECHA_FORMAT));
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        if (tipoMovimiento == null) {
            throw new IllegalArgumentException("El tipo de movimiento no puede ser nulo.");
        }

        this.tipoMovimiento = tipoMovimiento;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setEstado(OrdenMovimientoEstados estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }

        this.estado = estado;
    }

    public OrdenMovimientoEstados getEstado() {
        return estado;
    }

    public void agregarDetalle(DetalleMovimiento detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo.");
        }

        detalles.add(detalle);
    }

    public void agregarDetalles(List<DetalleMovimiento> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La lista de detalles no puede ser nula o vacía.");
        }

        for (DetalleMovimiento detalle : detalles) {
            agregarDetalle(detalle);
        }
    }

    public void eliminarDetalle(DetalleMovimiento detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo.");
        }

        detalles.remove(detalle);
    }

    public void eliminarDetalles(List<DetalleMovimiento> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La lista de detalles no puede ser nula o vacía.");
        }

        for (DetalleMovimiento detalle : detalles) {
            eliminarDetalle(detalle);
        }
    }

    public List<DetalleMovimiento> getDetalles() {
        return detalles;
    }
}
