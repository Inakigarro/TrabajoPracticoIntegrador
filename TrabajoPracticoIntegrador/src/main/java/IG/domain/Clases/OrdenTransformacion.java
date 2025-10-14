package IG.domain.Clases;

import IG.domain.Enums.TipoTransformacion;
import IG.domain.Enums.EstadosOrdenes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenTransformacion {
    private Integer id;
    private TipoTransformacion tipo;
    private LocalDateTime fecha;
    private EstadosOrdenes estado;
    private List<DetalleTransformacion> detalleTransformacionList;

    public OrdenTransformacion() {
        setId(0);
        setTipo(TipoTransformacion.SINDEFINIR);
        setFecha(LocalDateTime.now());
        setEstado(EstadosOrdenes.PENDIENTE);
        setDetalleTransformacionList(new ArrayList<>());
    }

    public OrdenTransformacion(Integer id, TipoTransformacion tipo, LocalDateTime fecha, EstadosOrdenes estado, List<DetalleTransformacion> detalleTransformacionList) {
        this();
        this.setId(id);
        this.setTipo(tipo);
        this.setFecha(fecha);
        this.setEstado(estado);
        this.agergarDetalles(detalleTransformacionList);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        this.id = id;
    }

    public TipoTransformacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransformacion tipo) {
        if (tipo == null || tipo == TipoTransformacion.SINDEFINIR) {
            throw new IllegalArgumentException("El tipo no puede ser nulo.");
        }
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.fecha = fecha;
    }

    public EstadosOrdenes getEstado() {
        return estado;
    }

    public void setEstado(EstadosOrdenes estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    public List<DetalleTransformacion> getDetalleTransformacionList() {
        return detalleTransformacionList;
    }

    public void setDetalleTransformacionList(List<DetalleTransformacion> detalleTransformacionList) {
        if (detalleTransformacionList == null) {
            throw new IllegalArgumentException("La lista de detalles no puede ser nula.");
        }
        this.detalleTransformacionList = detalleTransformacionList;
    }

    public void agregarDetalle(DetalleTransformacion detalle){
        detalleTransformacionList.add(detalle);
    }

    public void agergarDetalles(List<DetalleTransformacion> detalles) {
        if (detalles == null) {
            throw new IllegalArgumentException("La lista de detalles no puede ser nula");
        }
        for (DetalleTransformacion detalle : detalles) {
            agregarDetalle(detalle);
        }
    }

    public void aprobar() {
        if (this.estado != EstadosOrdenes.PENDIENTE) {
            throw new IllegalStateException("Solo se puede aprobar una orden pendiente");
        }
        this.estado = EstadosOrdenes.APROBADO;
    }

    public void ejecutar() {
        if (this.estado != EstadosOrdenes.APROBADO) {
            throw new IllegalStateException("Solo se puede ejecutar una orden aprobada");
        }
        this.estado = EstadosOrdenes.PROCESO;
    }

    public void cancelar() {
        if (this.estado == EstadosOrdenes.COMPLETADO || this.estado == EstadosOrdenes.CANCELADO) {
            throw new IllegalStateException("No se puede cancelar una orden finalizada o ya cancelada");
        }
        this.estado = EstadosOrdenes.CANCELADO;
    }
}
