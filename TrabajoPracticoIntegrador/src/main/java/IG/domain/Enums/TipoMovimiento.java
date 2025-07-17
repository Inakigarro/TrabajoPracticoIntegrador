package main.java.IG.domain.Enums;

public enum TipoMovimiento {
    SINDEFINIR("Sin definir"),
    INGRESO("Ingreso"),
    EGRESO("Egreso"),
    INTERNO("Interno");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
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
