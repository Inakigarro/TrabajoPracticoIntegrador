package IG.domain.Clases;

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
        this.id = id;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarDetalle(DetalleMovimiento detalle){
    detalleMovimientoList.add(detalle);
    }

    //public void aprobar(){

    //public void ejecutar(){

 }



