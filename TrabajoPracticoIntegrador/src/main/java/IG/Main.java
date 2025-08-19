package IG;

import IG.domain.Clases.OrdenMovimiento;
import IG.domain.Clases.ProductoUbicacion;
import IG.domain.DAO.OrdenMovimientoDAO;
import IG.domain.Enums.TipoMovimiento;
import main.java.IG.views.MainWindow;
import main.java.IG.config.ConexionBD;
import IG.domain.DAO.ProductoDAO;
import IG.domain.Clases.DetalleMovimiento;
import IG.domain.Clases.Producto;
import main.java.IG.domain.Enums.OrdenMovimientoEstados;

import javax.swing.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Mostrar la ventana principal (GUI)
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ventana Principal");
            MainWindow mainWindow = new MainWindow();
            frame.setContentPane(mainWindow.getContentPane());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // 1️⃣ Insertar productos distintos en la base
        try (Connection conn = ConexionBD.obtenerConexion()) {
            ProductoDAO productoDAO = new ProductoDAO(conn);

            Producto p1 = new Producto(null, "Coca Cola Zero", "Litros", 100.0);
            Producto p2 = new Producto(null, "Fanta", "Litros", 50.0);

            productoDAO.insertar(p1);
            productoDAO.insertar(p2);

            System.out.println("Productos insertados con IDs: " + p1.getId() + ", " + p2.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2️⃣ Listar todos los productos
        try (Connection conn = ConexionBD.obtenerConexion()) {
            ProductoDAO listarDAO = new ProductoDAO(conn);
            for (Producto producto : listarDAO.listarTodos()) {
                if (producto != null) {
                    System.out.println("Producto ID: " + producto.getId() +
                            ", Descripción: " + producto.getDescripcion() +
                            ", Stock: " + producto.getStock());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3️⃣ Crear DetalleMovimiento con stock válido
        List<DetalleMovimiento> detalleMovimiento = new ArrayList<>();

        // Producto 1
        ProductoUbicacion productoUbicacion1 = new ProductoUbicacion();
        productoUbicacion1.setId(1);
        Producto producto1 = new Producto(1, "Coca Cola Zero", "Litros", 100.0);
        productoUbicacion1.setProducto(producto1);

        DetalleMovimiento det1 = new DetalleMovimiento();
        det1.setProductoUbicacion(productoUbicacion1);
        det1.setEsSalida(false); // 👈 primero aclaramos que no es salida
        double cantidadDeseada1 = 10.0;
        det1.setCantidad(Math.min(cantidadDeseada1, producto1.getStock()));
        detalleMovimiento.add(det1);

        // Producto 2
        ProductoUbicacion productoUbicacion2 = new ProductoUbicacion();
        productoUbicacion2.setId(2);
        Producto producto2 = new Producto(2, "Fanta", "Litros", 50.0);
        productoUbicacion2.setProducto(producto2);

        DetalleMovimiento det2 = new DetalleMovimiento();
        det2.setProductoUbicacion(productoUbicacion2);
        det2.setEsSalida(false); // 👈 antes que nada
        double cantidadDeseada2 = 5.0;
        det2.setCantidad(cantidadDeseada2); // ahora no valida contra stock
        detalleMovimiento.add(det2);

        // 4️⃣ Insertar nueva orden de movimiento
        try (Connection conn = ConexionBD.obtenerConexion()) {
            OrdenMovimientoDAO ordenMovimientoDAO = new OrdenMovimientoDAO(conn);
            LocalDateTime fecha = LocalDateTime.now();

            OrdenMovimiento nuevaOrden = new OrdenMovimiento();
            nuevaOrden.setTipo(TipoMovimiento.INGRESO);
            nuevaOrden.setFecha(fecha);
            // ✅ Aseguramos que el valor del ENUM coincida con la DB
            nuevaOrden.setEstado(main.java.IG.domain.Enums.OrdenMovimientoEstados.APROBADO);
            nuevaOrden.setDetalleMovimientoList(detalleMovimiento);

            ordenMovimientoDAO.insertar(nuevaOrden);
            System.out.println("Nueva orden de movimiento insertada con ID: " + nuevaOrden.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 5️⃣ Listar todas las órdenes
        try (Connection conn = ConexionBD.obtenerConexion()) {
            OrdenMovimientoDAO listarOrdenesDAO = new OrdenMovimientoDAO(conn);
            for (OrdenMovimiento orden : listarOrdenesDAO.listarTodos()) {
                System.out.println("Orden ID: " + orden.getId() +
                        ", Tipo: " + orden.getTipo() +
                        ", Estado: " + orden.getEstado());
                if (orden.getDetalleMovimientoList() != null) {
                    for (DetalleMovimiento det : orden.getDetalleMovimientoList()) {
                        System.out.println("   Producto: " + det.getProductoUbicacion().getProducto().getDescripcion() +
                                ", Cantidad: " + det.getCantidad() +
                                ", EsSalida: " + det.esSalida());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
