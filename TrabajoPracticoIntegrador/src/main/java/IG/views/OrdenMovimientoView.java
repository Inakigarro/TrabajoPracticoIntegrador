package main.java.IG.views;

import main.java.IG.domain.Clases.DetalleMovimiento;
import main.java.IG.domain.Clases.OrdenMovimiento;
import main.java.IG.domain.Enums.TipoMovimiento;
import main.java.IG.domain.Enums.OrdenMovimientoEstados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenMovimientoView extends JFrame {

    private final JComboBox<TipoMovimiento> cbTipoMovimiento = new JComboBox<>(TipoMovimiento.values());
    private final JTextField txtProductoId = new JTextField();
    private final JTextField txtEstanteriaOrigen = new JTextField();
    private final JTextField txtEstanteriaDestino = new JTextField();
    private final JTextField txtCantidad = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Producto ID", "Est. Origen", "Est. Destino", "Cantidad"}, 0);
    private final JTable table = new JTable(tableModel);

    private final List<DetalleMovimiento> detalles = new ArrayList<>();
    private final OrdenMovimiento orden = new OrdenMovimiento();

    public OrdenMovimientoView() {
        setTitle("Órdenes de Movimiento");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agregar detalle"));
        formPanel.add(new JLabel("Tipo de movimiento:"));
        formPanel.add(cbTipoMovimiento);
        formPanel.add(new JLabel("Producto ID:"));
        formPanel.add(txtProductoId);
        formPanel.add(new JLabel("Estantería Origen:"));
        formPanel.add(txtEstanteriaOrigen);
        formPanel.add(new JLabel("Estantería Destino:"));
        formPanel.add(txtEstanteriaDestino);
        formPanel.add(new JLabel("Cantidad:"));
        formPanel.add(txtCantidad);

        JButton btnAgregar = new JButton("Agregar Detalle");
        JButton btnAprobar = new JButton("Aprobar Orden");
        JButton btnEjecutar = new JButton("Ejecutar Orden");

        formPanel.add(btnAgregar);
        formPanel.add(new JLabel());

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnAprobar);
        bottomPanel.add(btnEjecutar);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        btnAgregar.addActionListener(e -> agregarDetalle());
        btnAprobar.addActionListener(e -> aprobarOrden());
        btnEjecutar.addActionListener(e -> ejecutarOrden());
    }

    private void agregarDetalle() {
        try {
            String productoId = txtProductoId.getText().trim();
            String estOrigen = txtEstanteriaOrigen.getText().trim();
            String estDestino = txtEstanteriaDestino.getText().trim();
            String cantidad = txtCantidad.getText().trim();

            if (productoId.isEmpty() || cantidad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Producto y cantidad son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Solo validación de campos visibles (sin lógica de stock real)
            tableModel.addRow(new Object[]{productoId, estOrigen, estDestino, cantidad});
            txtProductoId.setText("");
            txtEstanteriaOrigen.setText("");
            txtEstanteriaDestino.setText("");
            txtCantidad.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar detalle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aprobarOrden() {
        try {
            orden.setId(1); // simulado
            orden.setTipo((TipoMovimiento) cbTipoMovimiento.getSelectedItem());
            orden.setFecha(LocalDateTime.now());
            orden.setEstado(OrdenMovimientoEstados.APROBADO);
            JOptionPane.showMessageDialog(this, "Orden aprobada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al aprobar orden: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarOrden() {
        try {
            if (orden.getEstado() != OrdenMovimientoEstados.APROBADO) {
                throw new IllegalStateException("Solo se puede ejecutar una orden aprobada.");
            }

            orden.setEstado(OrdenMovimientoEstados.PROCESO);
            JOptionPane.showMessageDialog(this, "Orden ejecutada (simulado).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al ejecutar orden: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


