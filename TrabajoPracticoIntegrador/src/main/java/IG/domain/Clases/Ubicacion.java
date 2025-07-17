package main.java.IG.domain.Clases;

import java.util.ArrayList;
import java.util.List;

public class Ubicacion {
    private int nroEstanteria;
    private double capacidadUsada;
    private List<ProductoUbicacion> productos;

    public Ubicacion(int nroEstanteria, double capacidadUsada) {
        this();
        setNroEstanteria(nroEstanteria);
        setCapacidadUsada(capacidadUsada);
    }

    public Ubicacion() {
        setNroEstanteria(1);
        setCapacidadUsada(0.0);
        addRangeProductos(new ArrayList<>());
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

    public List<ProductoUbicacion> getProductos() {
        return productos;
    }

    public void addProducto(ProductoUbicacion producto) {
        if (producto == null)
            throw new IllegalArgumentException("El producto no puede ser nulo.");

        this.productos.add(producto);
    }

    public void addRangeProductos(List<ProductoUbicacion> productos) {
        if (productos == null || productos.isEmpty()) {
            throw new IllegalArgumentException("La lista de productos no puede ser nula ni vacía.");
        }
        this.productos.addAll(productos);
    }

    public boolean estaDisponible() {
        return 1250d - capacidadUsada > 0;
    }
}
