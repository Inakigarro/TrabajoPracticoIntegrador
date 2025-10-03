package IG.domain.Enums;

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

    public static TipoMovimiento fromDescripcion(String descripcion) {
        for (TipoMovimiento tm : values()) {
            if (tm.getDescripcion().equalsIgnoreCase(descripcion)) {
                return tm;
            }
        }
        throw new IllegalArgumentException("No existe TipoMovimiento para la descripción: " + descripcion);
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
