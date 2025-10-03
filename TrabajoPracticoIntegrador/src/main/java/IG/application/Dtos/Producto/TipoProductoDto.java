package IG.application.Dtos.Producto;

import IG.domain.Clases.TipoProducto;

public record TipoProductoDto(Integer id, String descripcion) {
    public static TipoProductoDto map(TipoProducto tipo) {
        return new TipoProductoDto(tipo.getId(),tipo.getDescripcion());
    }

    @Override
    public String toString() {
        return this.descripcion;
    }
}
