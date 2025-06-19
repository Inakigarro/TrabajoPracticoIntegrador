package main.java.IG.domain.Enums;

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

    @Override
    public String toString() {
        return "TipoZona{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}
