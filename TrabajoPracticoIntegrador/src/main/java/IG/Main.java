package IG;

import IG.domain.DAO.OrdenMovimientoDAO;
import IG.domain.DAO.OrdenTransformacionDAO;
import IG.domain.DAO.ProductoUbicacionDAO;
import IG.views.MainWindow;
import IG.config.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });


        try (Connection conn = ConexionBD.obtenerConexionMySQL()) {
            System.out.println("Inicializando base de datos...");
            System.out.println("Creando base de datos si no existe...");
            var stmt = conn.prepareStatement("""
                CREATE DATABASE IF NOT EXISTS `trabajoIntegrador`
            """);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("Base de datos creada o ya existía.");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try (Connection conn = ConexionBD.obtenerConexionBaseDatos()) {
            ProductoUbicacionDAO productoUbicacionDao = new ProductoUbicacionDAO(conn);
            productoUbicacionDao.inicializacion();
            OrdenMovimientoDAO ordenmovimientoDAO = new OrdenMovimientoDAO(conn);
            ordenmovimientoDAO.inicializacion();
            OrdenTransformacionDAO ordentransformacionDAO = new OrdenTransformacionDAO(conn);
            ordentransformacionDAO.inicializacion();
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}