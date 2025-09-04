package IG;

import IG.domain.DAO.OrdenMovimientoDAO;
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
            OrdenMovimientoDAO ordenmovimientoDAO = new OrdenMovimientoDAO(conn);
            ordenmovimientoDAO.inicializacion();
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}