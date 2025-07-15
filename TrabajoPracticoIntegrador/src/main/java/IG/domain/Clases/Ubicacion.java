package main.java.IG.domain.Clases;

import java.util.ArrayList;
import java.util.List;

public class Ubicacion {
    private int nroEstanteria;
    private double capacidadUsada;
    private List<Producto>productos;

    public Ubicacion(int nroEstanteria, double capacidadUsada, List<main.java.IG.domain.Clases.Producto> productos) {
        this.nroEstanteria = nroEstanteria;
        this.capacidadUsada = capacidadUsada;
        this.productos = productos;
    }

    public Ubicacion() {
        setNroEstanteria(1);
        setCapacidadUsada(0.0);
        setProductos(new ArrayList<>());
    }

    public int getNroEstanteria() {
        return nroEstanteria;
    }

    public void setNroEstanteria(int nroEstanteria) {
        if (nroEstanteria <= 0) {
            throw new IllegalArgumentException("El número de estantería debe ser mayor a cero.");
        }
        this.nroEstanteria = nroEstanteria;
    }

    public double getCapacidadUsada() {
        return capacidadUsada;
    }

    public void setCapacidadUsada(double capacidadUsada) {
        if (capacidadUsada < 0) {
            throw new IllegalArgumentException("La capacidad usada no puede ser negativa.");
        }
        this.capacidadUsada = capacidadUsada;
    }

    public List<main.java.IG.domain.Clases.Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<main.java.IG.domain.Clases.Producto> productos) {
        if (productos == null) {
            throw new IllegalArgumentException("La lista de productos no puede ser nula.");
        }
        this.productos = productos;
    }

    //public int calcularCapacidadMaxima(){

    public void almacenarProducto(main.java.IG.domain.Clases.Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("No se puede almacenar un producto nulo.");
        }
        productos.add(producto);
    }

    public boolean estaDisponible() {
        return 1450d - capacidadUsada > 0;
    }
}
