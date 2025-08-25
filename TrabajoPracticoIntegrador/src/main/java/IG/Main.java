package IG;

import main.java.IG.views.MainWindow;
import main.java.IG.config.ConexionBD;
import main.java.IG.domain.Clases.Producto;
import main.java.IG.domain.DAO.ProductoDAO;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });


        try (Connection conn = ConexionBD.obtenerConexion()) {
            ProductoDAO productoDAO = new ProductoDAO(conn);

            Producto nuevo = new Producto(null, "Coca Cola Zero", "Litros", 100.0);
            productoDAO.insertar(nuevo);

            System.out.println("Producto insertado con ID: " + nuevo.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = ConexionBD.obtenerConexion()) {
            ProductoDAO listarDAO = new ProductoDAO(conn);
            for (Producto producto : listarDAO.listarTodos()) {
                System.out.println("Producto ID: " + producto.getId() + ", Descripción: " + producto.getDescripcion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}