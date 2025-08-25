package IG.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Sistema de Gestión de Almacenes (WMS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnProductos = new JButton("Gestión de Productos");
        JButton btnUbicaciones = new JButton("Gestión de Ubicaciones");
        JButton btnMovimientos = new JButton("Órdenes de Movimiento");
        JButton btnTransformaciones = new JButton("Órdenes de Transformación");
        JButton btnHistorial = new JButton("Ver Historial de Movimientos");
        JButton btnSalir = new JButton("Salir");

        //Si se quiere cerrar la ventana anterior al abrir una nueve, agregar en cada btn this.dispose()

        btnProductos.addActionListener((ActionEvent e) -> {
            new ProductManagementView().setVisible(true);
        });

        btnUbicaciones.addActionListener(e -> {
            new UbicacionManagementView().setVisible(true);
        });

        btnMovimientos.addActionListener(e -> {
            new OrdenMovimientoView().setVisible(true);
        });

        btnTransformaciones.addActionListener(e -> {
            new OrdenTransformacionView().setVisible(true);
        });

        btnHistorial.addActionListener(e -> new VerHistorialViews().setVisible(true));

        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(btnProductos);
        panel.add(btnUbicaciones);
        panel.add(btnMovimientos);
        panel.add(btnTransformaciones);
        panel.add(btnHistorial);
        panel.add(btnSalir);

        add(panel);
    }
}
