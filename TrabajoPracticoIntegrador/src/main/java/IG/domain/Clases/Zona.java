package IG.domain.Clases;

import IG.domain.Enums.TipoZona;

import java.util.ArrayList;
import java.util.List;

public class Zona {
    private TipoZona tipo;
    private List<Ubicacion> ubicaciones;

    public Zona() {
        this.tipo = TipoZona.ENTRADA;
        this.ubicaciones = new ArrayList<>();
    }

    public Zona(TipoZona tipo, List<Ubicacion> ubicaciones) {
        this.tipo = tipo;
        this.ubicaciones = ubicaciones;
    }

    public List<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<Ubicacion> ubicaciones) {
        if (ubicaciones == null) {
            throw new IllegalArgumentException("La lista de ubicaciones no puede ser nulo");
        }
        this.ubicaciones = ubicaciones;
    }

    public TipoZona getTipo() {
        return tipo;
    }

    public void setTipo(TipoZona tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de zona no puede ser nulo");
        }
        this.tipo = tipo;
    }
}
