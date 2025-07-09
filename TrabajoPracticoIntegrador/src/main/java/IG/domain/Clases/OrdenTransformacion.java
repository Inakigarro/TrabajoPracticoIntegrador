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
    }

    public OrdenTransformacion(String id, TipoTransformacion tipo, LocalDateTime fecha, List<DetalleTransformacion> detalleTransformacionList) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.detalleTransformacionList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoTransformacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransformacion tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<DetalleTransformacion> getDetalleTransformacionList() {
        return detalleTransformacionList;
    }

    public void setDetalleTransformacionList(List<DetalleTransformacion> detalleTransformacionList) {
        this.detalleTransformacionList = detalleTransformacionList;
    }

    public void agregarDetalle(DetalleTransformacion detalle){
        detalleTransformacionList.add(detalle);
    }

    //reenvasar

}
