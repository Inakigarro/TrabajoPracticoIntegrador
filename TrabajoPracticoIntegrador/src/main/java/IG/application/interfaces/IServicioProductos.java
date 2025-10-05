package IG.application.interfaces;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Producto.TipoProductoDto;

import java.sql.SQLException;
import java.util.List;

public interface IServicioProductos {
    public ProductoDto crearProducto(ProductoDto productoDto) throws SQLException;
    public TipoProductoDto crearTipoProducto(String descripcion) throws SQLException;
    public List<TipoProductoDto> obtenerTiposProductos() throws SQLException;
    public List<ProductoDto> obtenerProductos() throws SQLException;
}
