package IG.domain.Clases;

import IG.domain.Enums.TipoTransformacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenTransformacion {
    private String id;
    private TipoTransformacion tipo;
    private LocalDateTime fecha;
    private List<DetalleTransformacion>detalleTransformacionList;

    public OrdenTransformacion() {
        setId("SIN ID");
        setTipo(TipoTransformacion.SINDEFINIR);
        setFecha(LocalDateTime.now());
        setDetalleTransformacionList(new ArrayList<>());
    }

    public OrdenTransformacion(String id, TipoTransformacion tipo, LocalDateTime fecha, List<DetalleTransformacion> detalleTransformacionList) {
        this();
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.detalleTransformacionList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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