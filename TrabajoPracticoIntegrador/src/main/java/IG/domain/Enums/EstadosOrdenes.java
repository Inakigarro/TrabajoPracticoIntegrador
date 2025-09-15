package IG.domain.Enums;

public enum EstadosOrdenes {
    PENDIENTE("Pendiente"),
    APROBADO("Aprobado"),
    PROCESO("En Proceso"),
    COMPLETADO("Completado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadosOrdenes(String descripcion) {
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

    public static EstadosOrdenes fromDescripcion(String descripcion) {
        for (EstadosOrdenes estado : values()) {
            if (estado.getDescripcion().equalsIgnoreCase(descripcion)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + descripcion);
    }
}
