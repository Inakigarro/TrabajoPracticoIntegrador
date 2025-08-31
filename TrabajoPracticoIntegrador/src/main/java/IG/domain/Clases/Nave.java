package IG.domain.Clases;

import java.util.ArrayList;
import java.util.List;

public class Nave {
    private Integer id;
    private List<Zona> zonas;

    public Nave() {
        this.id = 0;
        this.zonas = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id < 1)
            throw new IllegalArgumentException("El id no puede ser nulo o menor a 1.");

        this.id = id;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void agregarZona(Zona zona) {
        if (zona == null) {
            throw new IllegalArgumentException("La zona no puede ser nula.");
        }
        if (zonas.contains(zona)) {
            throw new IllegalArgumentException("La zona ya existe en la nave.");
        }
        this.zonas.add(zona);
        zona.setNave(this);
    }

    public void agregarZonas(List<Zona> zonas) {
        if (zonas == null) {
            throw new IllegalArgumentException("La lista de zonas no puede ser nula.");
        }
        for (Zona zona : zonas) {
            agregarZona(zona);
        }
    }
}
