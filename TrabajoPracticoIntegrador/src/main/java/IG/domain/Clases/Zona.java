package main.java.IG.domain.Clases;

import IG.domain.Enums.TipoZona;

import java.util.List;

public class Zona {
    private TipoZona tipo;
    private List<Ubicacion> ubicaciones;

    public Zona() {
    }

    public Zona(TipoZona tipo, List<Ubicacion> ubicaciones) {
        this.tipo = tipo;
        this.ubicaciones = ubicaciones;
    }

    public List<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<Ubicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public TipoZona getTipo() {
        return tipo;
    }

    public void setTipo(TipoZona tipo) {
        this.tipo = tipo;
    }
}
