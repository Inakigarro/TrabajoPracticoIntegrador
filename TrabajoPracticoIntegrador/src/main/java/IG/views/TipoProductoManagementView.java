package IG.views;

import IG.application.interfaces.IServicioProductos;
import IG.application.ServicioProductosDao;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TipoProductoManagementView extends JFrame {
    private final JTextField txtDescripcion = new JTextField();
    private final JButton btnGuardar = new JButton("Guardar");
    private final IServicioProductos servicio;

    public TipoProductoManagementView() {
        this.servicio = new ServicioProductosDao();
        setTitle("Agregar Tipo de Producto");
        setSize(350, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel());
        panel.add(btnGuardar);
        add(panel, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarTipoProducto());
    }

    private void guardarTipoProducto() {
        String descripcion = txtDescripcion.getText().trim();
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            servicio.crearTipoProducto(descripcion);
            JOptionPane.showMessageDialog(this, "Tipo de producto creado exitosamente.");
            this.dispose();
            this.setVisible(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de SQL", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }
}

