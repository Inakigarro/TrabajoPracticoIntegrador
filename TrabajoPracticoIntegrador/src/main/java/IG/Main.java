package IG;

import IG.domain.DAO.ProductoUbicacionDAO;
import IG.views.MainWindow;
import IG.config.ConexionBD;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });


        try (Connection conn = ConexionBD.obtenerConexion()) {
            System.out.println("Inicializando base de datos...");
            ProductoUbicacionDAO productoUbicacionDao = new ProductoUbicacionDAO(conn);
            productoUbicacionDao.inicializacion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}