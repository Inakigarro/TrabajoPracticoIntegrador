package main.java.IG.views;

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

        btnProductos.addActionListener((ActionEvent e) -> {
            new ProductManagementView().setVisible(true);
            this.dispose();
        });

        btnUbicaciones.addActionListener(e -> {
            new UbicacionManagementView().setVisible(true);
            this.dispose();
        });

        btnMovimientos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Órdenes de Movimiento"));
        btnTransformaciones.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Órdenes de Transformación"));
        btnHistorial.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Historial de Movimientos"));
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
