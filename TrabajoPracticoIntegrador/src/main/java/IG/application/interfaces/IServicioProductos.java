package IG.application.interfaces;

import IG.application.Dtos.Producto.ProductoDto;

import java.sql.SQLException;
import java.util.List;

public interface IServicioProductos {
    public ProductoDto crearProducto(ProductoDto productoDto) throws SQLException;
    public List<ProductoDto> obtenerProductos() throws SQLException;
}
