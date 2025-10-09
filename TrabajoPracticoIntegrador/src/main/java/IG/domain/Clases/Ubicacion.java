package IG.domain.Clases;

import IG.application.Dtos.Ubicacion.UbicacionDto;
import IG.domain.Constants.UbicacionConstants;

import java.util.ArrayList;
import java.util.List;

public class Ubicacion {
    private Integer id;
    private Zona zona;
    private Integer nroEstanteria;
    private Integer nroNivel;
    private double capacidadUsada;
    private final List<ProductoUbicacion> productos;

    public Ubicacion(Integer nroEstanteria, Integer nroNivel, Zona zona) {
        this();
        setNroEstanteria(nroEstanteria);
        setNroNivel(nroNivel);
        setZona(zona);
    }

    public Ubicacion() {
        this.id = 0;
        this.nroEstanteria = 0;
        this.nroNivel = 0;
        this.capacidadUsada = 0.0;
        this.productos = new ArrayList<>();
    }

    public void setId(Integer id){
        if (id == null || id < 1)
            throw new IllegalArgumentException("El id no puede ser nulo.");

        this.id = id;
    }

    public Integer getId() {
        return id;
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

    public int getNroNivel() {
        return nroNivel;
    }

    public void setNroNivel(int nroNivel) {
        if (nroNivel <= 0) {
            throw new IllegalArgumentException("El número de nivel debe ser mayor a cero.");
        }
        this.nroNivel = nroNivel;
    }

    public double getCapacidadUsada() {
        return capacidadUsada;
    }

    public void setCapacidadUsada(double capacidadUsada) {
        if (capacidadUsada < UbicacionConstants.UBICACION_CAPACIDAD_MIN) {
            throw new IllegalArgumentException("La capacidad usada no puede ser negativa.");
        }
        this.capacidadUsada = capacidadUsada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        if (zona == null) {
            throw new IllegalArgumentException("La zona no puede ser nula.");
        }
        this.zona = zona;
    }

    public List<ProductoUbicacion> getProductos() {
        return productos;
    }

    public void addProducto(ProductoUbicacion productoUbicacion) {
        if (productoUbicacion == null)
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        productoUbicacion.setUbicacion(this);
        this.productos.add(productoUbicacion);
    }

    public void addRangeProductos(List<ProductoUbicacion> productos) {
        if (productos == null) {
            throw new IllegalArgumentException("La lista de productos no puede ser nula.");
        }
        productos.forEach(this::addProducto);
    }

    public boolean estaDisponible() {
        return UbicacionConstants.UBICACION_CAPACIDAD_MAX - capacidadUsada > 0;
    }

    public ProductoUbicacion getProductoUbicacionPorProductoId(int productoId) {
        for (ProductoUbicacion pu : productos) {;
            if (pu.getProducto() != null && pu.getProducto().getId() == productoId) {
                return pu;
            }
        }
        return null;
    }

    public boolean tieneProducto(Producto producto) {
        if (producto == null) return false;
        return this.productos.stream().anyMatch(p -> p.getProducto().equals(producto));
    }

    public Double getCapacidadDisponible() {
        return UbicacionConstants.UBICACION_CAPACIDAD_MAX - capacidadUsada;
    }

    public boolean tieneCapacidadDisponible(double cantidad) {
        return (capacidadUsada + cantidad) <= UbicacionConstants.UBICACION_CAPACIDAD_MAX;
    }

    public boolean tieneStockDisponible(Integer productoId, double cantidad) {
        ProductoUbicacion pu = getProductoUbicacionPorProductoId(productoId);
        return pu != null && pu.getStockProductoUbicacion() >= cantidad;
    }

    public double getStockProducto(Producto producto) {
        if (producto == null) return 0.0;
        for (ProductoUbicacion pu : productos) {
            if (pu.getProducto() != null && pu.getProducto().getId() == producto.getId()) {
                return pu.getStockProductoUbicacion();
            }
        }
        return 0.0;
    }

    public static Ubicacion map(UbicacionDto dto) {
        var ubicacion = new Ubicacion();
        ubicacion.setId(dto.id());
        ubicacion.setNroEstanteria(dto.nroEstanteria());
        ubicacion.setNroNivel(dto.nroNivel());
        ubicacion.setCapacidadUsada(dto.capacidadUsada());
        return ubicacion;
    }

    @Override
    public String toString() {
        return "Ubicacion{" +
                "nroEstanteria=" + nroEstanteria +
                ", nroNivel=" + nroNivel +
                ", capacidadUsada=" + capacidadUsada +
                ", productos=" + productos +
                '}';
    }
}
