package IG.domain.Clases;

import IG.application.Dtos.Ubicacion.ZonaDto;
import IG.domain.Enums.TipoZona;

import java.util.ArrayList;
import java.util.List;

public class Zona {
    private Integer id;
    private TipoZona tipo;
    private Nave nave;
    private List<Ubicacion> ubicaciones;

    public Zona() {
        this.id = 0;
        this.tipo = TipoZona.ENTRADA;
        this.nave = null;
        this.ubicaciones = new ArrayList<>();
    }

    public Zona(TipoZona tipo) {
        this();
        this.setTipo(tipo);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("El id no puede ser nulo o menor a 1.");
        }
        this.id = id;
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

    public Nave getNave() {
        return nave;
    }

    public void setNave(Nave nave) {
        if (nave == null) {
            throw new IllegalArgumentException("La nave no debe ser nula");
        }
        this.nave = nave;
    }

    public List<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void agregarUbicacion(Ubicacion ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException("La ubicación no puede ser nula.");
        }
        this.ubicaciones.add(ubicacion);
    }

    public void agregarUbicaciones(List<Ubicacion> ubicaciones) {
        if (ubicaciones == null || ubicaciones.contains(null)) {
            throw new IllegalArgumentException("La lista de ubicaciones no puede ser nula ni contener elementos nulos.");
        }
        this.ubicaciones.addAll(ubicaciones);
    }

    public static Zona map(ZonaDto dto) {
        var zona = new Zona();
        zona.setId(dto.id());
        zona.setTipo(TipoZona.fromDescription(dto.tipo()));
        return zona;
    }
}
