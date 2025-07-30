package main.java.IG;

import main.java.IG.domain.Clases.*;
import main.java.IG.domain.Enums.OrdenMovimientoEstados;
import main.java.IG.domain.Enums.TipoMovimiento;
import main.java.IG.views.MainWindow;

import javax.swing.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Ventana Principal");
//            MainWindow mainWindow = new MainWindow();
//
//            frame.setContentPane(mainWindow.getContentPane());
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });

        TipoProducto tipoYerba = new TipoProducto(1, "Yerba Mate");
        System.out.println(tipoYerba);

        Producto yerbaRosamonte = new Producto(1, "Rosamonte Suave", 500d, "g", 0d, tipoYerba);
        System.out.println(yerbaRosamonte);

        Producto yerbaTaragui = new Producto(2, "Taragui Tradicional", 500d, "g", 0d, tipoYerba);
        System.out.println(yerbaTaragui);

        Ubicacion ubicacion1 = new Ubicacion(1, 1);
        Ubicacion ubicacion2 = new Ubicacion(1, 2);

        DetalleMovimiento detalleMovimiento1 = new DetalleMovimiento(yerbaRosamonte, 100d, ubicacion1, false);
        DetalleMovimiento detalleMovimiento2 = new DetalleMovimiento(yerbaTaragui, 100d, ubicacion2, false);

        OrdenMovimiento ordenMovimiento = new OrdenMovimiento(1, TipoMovimiento.INGRESO, LocalDateTime.now(), OrdenMovimientoEstados.PENDIENTE);
        ordenMovimiento.agregarDetalle(detalleMovimiento1);
        ordenMovimiento.agregarDetalle(detalleMovimiento2);
        System.out.println(ordenMovimiento);
    }
}