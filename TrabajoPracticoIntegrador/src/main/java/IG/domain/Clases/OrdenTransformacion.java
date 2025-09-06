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
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.estado = estado != null ? estado : EstadosOrdenes.PENDIENTE;
        this.detalleTransformacionList = detalleTransformacionList != null ? detalleTransformacionList : new ArrayList<>();
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

    //reenvasar
}
