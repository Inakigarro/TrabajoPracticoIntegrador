package main.java.IG.domain.Enums;

public enum OrdenMovimientoEstados {
    PENDIENTE("Pendiente"),
    PROCESO("En Proceso"),
    COMPLETADO("Completado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    OrdenMovimientoEstados(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni vacía.");
        }
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
