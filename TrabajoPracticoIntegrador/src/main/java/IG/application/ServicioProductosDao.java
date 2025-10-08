package IG.application;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Producto.TipoProductoDto;
import IG.application.interfaces.IServicioProductos;
import IG.config.ConexionBD;
import IG.domain.Clases.Producto;
import IG.domain.Clases.TipoProducto;
import IG.domain.DAO.ProductoUbicacionDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServicioProductosDao implements IServicioProductos {
    public ServicioProductosDao() {

    }

    @Override
    public ProductoDto crearProducto(ProductoDto productoDto) throws SQLException {
        try(Connection conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);

            Producto nuevoProducto = productoUbicacionDAO.insertarProducto(Producto.map(productoDto));
            return ProductoDto.map(nuevoProducto);
        } catch (SQLException exception) {
            String error = "Error al crear el producto: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }

    @Override
    public TipoProductoDto crearTipoProducto(String descripcion) throws SQLException {
        try(Connection conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);
            TipoProducto nuevoTipoProducto = productoUbicacionDAO.insertarTipoProducto(descripcion);
            return new TipoProductoDto(nuevoTipoProducto.getId(), nuevoTipoProducto.getDescripcion());
        } catch (SQLException exception) {
            String error = "Error al crear el tipo de producto: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }

    @Override
    public List<TipoProductoDto> obtenerTiposProductos() throws SQLException {
        try(Connection conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);
            List<TipoProducto> tiposProductos = productoUbicacionDAO.listarTodosTiposProductos();
            return tiposProductos.stream().map(tp -> new TipoProductoDto(tp.getId(), tp.getDescripcion())).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener los tipos de productos: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }

    @Override
    public List<ProductoDto> obtenerProductos() throws SQLException {
        try(Connection conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);
            List<Producto> productos = productoUbicacionDAO.listarTodosProductos();
            return productos.stream().map(ProductoDto::map).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener los productos: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }

    @Override
    public void actualizarProducto(Producto producto) throws SQLException {
        try(Connection conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);
            productoUbicacionDAO.actualizarProducto(producto);
        } catch (SQLException exception) {
            String error = "Error al actualizar el producto: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }
}
