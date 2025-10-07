package IG.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/trabajoIntegrador?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Zaaky78_6";

    public static Connection obtenerConexionMySQL() throws SQLException {;
        return DriverManager.getConnection(MYSQL_URL, USER, PASSWORD);
    }

    public static Connection obtenerConexionBaseDatos() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }
}
