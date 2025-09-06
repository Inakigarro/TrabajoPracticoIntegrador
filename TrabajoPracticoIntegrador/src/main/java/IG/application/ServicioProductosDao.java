package IG.application;

import IG.application.Dtos.ProductoDto;
import IG.application.Dtos.TipoProductoDto;
import IG.config.ConexionBD;
import IG.domain.Clases.Producto;
import IG.domain.Clases.TipoProducto;
import IG.domain.DAO.ProductoUbicacionDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServicioProductosDao implements IServicioProductos{
    private ConexionBD conexionBD;

    public ServicioProductosDao() {
        this.conexionBD = new ConexionBD();
    }

    @Override
    public ProductoDto crearProducto(ProductoDto productoDto) throws SQLException {
        try(Connection conn = this.conexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);
            TipoProducto tipoProducto = productoUbicacionDAO.buscarTipoProductoPorId(productoDto.tipoProducto().id());
            if (tipoProducto == null) {
                tipoProducto = productoUbicacionDAO.insertarTipoProducto(productoDto.tipoProducto().descripcion());
            }
            Producto nuevoProducto = productoUbicacionDAO.insertarProducto(
                    new Producto(productoDto.descripcion(),
                            productoDto.cantidadUnidad(),
                            productoDto.unidadMedida(),
                            productoDto.stock(),
                            tipoProducto)
            );
            return new ProductoDto(
                    nuevoProducto.getId(),
                    nuevoProducto.getDescripcion(),
                    nuevoProducto.getCantidadUnidad(),
                    nuevoProducto.getUnidadMedida(),
                    nuevoProducto.getStock(),
                    new TipoProductoDto(tipoProducto.getId(), tipoProducto.getDescripcion())
            );
        } catch (SQLException exception) {
            String error = "Error al crear el producto: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }

    @Override
    public List<ProductoDto> obtenerProductos() throws SQLException {
        try(Connection conn = this.conexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDAO = new ProductoUbicacionDAO(conn);
            List<Producto> productos = productoUbicacionDAO.listarProductos(1, 100);
            return productos.stream().map(producto -> new ProductoDto(
                    producto.getId(),
                    producto.getDescripcion(),
                    producto.getCantidadUnidad(),
                    producto.getUnidadMedida(),
                    producto.getStock(),
                    new TipoProductoDto(
                            producto.getTipoProducto().getId(),
                            producto.getTipoProducto().getDescripcion()
                    )
            )).toList();
        } catch (SQLException exception) {
            String error = "Error al obtener los productos: " + exception.getMessage();
            System.out.println(error);
            throw new SQLException(error, exception);
        }
    }
}
