package main.java.IG.domain.Enums;

public enum TipoTransformacion {
    SINDEFINIR("Sin definir"),
    REENVASADO("Revenasado"),
    FRACCIONAMIENTO("Fraccionamiento");

    private final String descripcion;

    TipoTransformacion(String descripcion) {
        if(descripcion == null || descripcion.trim().isEmpty()){
            throw new IllegalArgumentException("La descripcion no puede estar vacia.");
        }
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "TipoTransformacion{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}
