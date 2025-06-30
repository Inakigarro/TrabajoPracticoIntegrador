package main.java.IG.domain.Enums;

public enum TipoDetalleTransformacion {
    SINDEFININR("Sin definir"),
    ENTRADA("Entrada"),
    SALIDA("Salida"),
    ;

    private final String descripcion;

    TipoDetalleTransformacion(String descripcion) {
        if(descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion no puede ser nula");
        }
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "TipoDetalleTransformacion{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}
