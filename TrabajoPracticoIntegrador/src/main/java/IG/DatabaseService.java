package IG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseService {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream input = DatabaseService.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                url = prop.getProperty("mysql.url");
                user = prop.getProperty("mysql.user");
                password = prop.getProperty("mysql.password");
            } else {
                throw new RuntimeException("No se encontró el archivo db.properties");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error al leer db.properties", ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

