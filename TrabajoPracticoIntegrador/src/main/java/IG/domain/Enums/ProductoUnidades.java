package IG.domain.Enums;

public enum ProductoUnidades {
    SIN_DEFINIR("Sin definir"),
    KILOGRAMOS("Kilogramos"),
    LITROS("Litros"),
    GRAMOS("Gramos"),
    MILILITROS("Mililitros");

    private final String descripcion;

    ProductoUnidades(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni vacía.");
        }
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static ProductoUnidades fromDescription(String descripcion) {
        for (ProductoUnidades unidad : values()) {
            if (unidad.getDescripcion().equalsIgnoreCase(descripcion)) {
                return unidad;
            }
        }
        throw new IllegalArgumentException("Unidad no válida: " + descripcion);
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
