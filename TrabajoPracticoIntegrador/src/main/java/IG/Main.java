package IG;

import IG.domain.Clases.TipoProducto;
import IG.views.MainWindow;
import IG.config.ConexionBD;
import IG.domain.Clases.Producto;
import IG.domain.DAO.ProductoDAO;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });


        try (Connection conn = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión exitosa a la base de datos.");
            System.out.println("creacion productoDAO");
            ProductoDAO productoDAO = new ProductoDAO(conn);

            System.out.println("creacion nuevo tipo producto");
            TipoProducto nuevoTipoProducto = new TipoProducto("Botella");
            productoDAO.insertarTipoProducto(nuevoTipoProducto);
            System.out.println("creacion nuevo producto");
            Producto nuevo = new Producto("Coca Cola Zero", 1.5d, "Litros", 100.00, nuevoTipoProducto);

            System.out.println("insertando producto");
            productoDAO.insertarProducto(nuevo);

            System.out.println("Producto insertado con ID: " + nuevo.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión exitosa a la base de datos.");

            System.out.println("Listando todos los productos:");
            ProductoDAO listarDAO = new ProductoDAO(conn);
            for (Producto producto : listarDAO.listarTodos()) {
                System.out.println(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}