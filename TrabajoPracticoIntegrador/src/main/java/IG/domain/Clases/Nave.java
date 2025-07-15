package main.java.IG.domain.Clases;

import java.util.List;

public class Nave {
    private List<Zona> zonas;

    public Nave() {
    }

    public Nave(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }
}
