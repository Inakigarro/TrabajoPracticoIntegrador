package main.java.IG.domain.Clases;

import java.util.List;

public class Ubicacion {
    private int nroEstanteria;
    private double capacidadUsada;
    private List<Producto>productos;

    public Ubicacion(int nroEstanteria, double capacidadUsada, List<Producto> productos) {
        this.nroEstanteria = nroEstanteria;
        this.capacidadUsada = capacidadUsada;
        this.productos = productos;
    }

    public Ubicacion() {
    }

    public int getNroEstanteria() {
        return nroEstanteria;
    }

    public void setNroEstanteria(int nroEstanteria) {
        this.nroEstanteria = nroEstanteria;
    }

    public double getCapacidadUsada() {
        return capacidadUsada;
    }

    public void setCapacidadUsada(double capacidadUsada) {
        this.capacidadUsada = capacidadUsada;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    //public int calcularCapacidadMaxima(){

    //public void almacernarProducto(){

    public boolean estaDisponible() {
        return 1450d - capacidadUsada > 0;
    }
}
