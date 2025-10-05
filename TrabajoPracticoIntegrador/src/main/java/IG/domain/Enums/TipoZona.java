package IG.domain.Enums;

public enum TipoZona {
    ENTRADA("Entrada"),
    SALIDA("Salida"),
    TRANSFORMACION("Transformacion"),
    ALMACENAMIENTO("Almacenamiento"),;

    private final String descripcion;

    TipoZona(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()){
            throw new IllegalArgumentException("La descripcion no puede estar vacia");
        }
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoZona fromDescription(String descripcion) {
        for (TipoZona tipo : TipoZona.values()) {
            if (tipo.getDescripcion().equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No existe un tipo de zona con la descripcion: " + descripcion);
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
