package main.java.IG.views;

import main.java.IG.domain.Enums.EstadoOrdenTransformacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.time.LocalDateTime;

public class OrdenTransformacionView extends JFrame {

    private final JComboBox<IG.domain.Enums.TipoTransformacion> cbTipoTransformacion =
            new JComboBox<>(IG.domain.Enums.TipoTransformacion.values());
    private final JTextField txtProductoOrigen = new JTextField();
    private final JTextField txtUbicacion = new JTextField();
    private final JTextField txtProductoResultado = new JTextField();
    private final JTextField txtCantidadResultado = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Tipo", "Prod. Origen", "Ubicación", "Prod. Resultado", "Cantidad"}, 0);
    private final JTable table = new JTable(tableModel);

    private EstadoOrdenTransformacion estadoActual = EstadoOrdenTransformacion.PENDIENTE;

    public OrdenTransformacionView() {
        setTitle("Órdenes de Transformación");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agregar detalle de transformación"));

        formPanel.add(new JLabel("Tipo de transformación:"));
        formPanel.add(cbTipoTransformacion);
        formPanel.add(new JLabel("Producto origen (ID):"));
        formPanel.add(txtProductoOrigen);
        formPanel.add(new JLabel("Ubicación:"));
        formPanel.add(txtUbicacion);
        formPanel.add(new JLabel("Producto resultante:"));
        formPanel.add(txtProductoResultado);
        formPanel.add(new JLabel("Cantidad resultante:"));
        formPanel.add(txtCantidadResultado);

        JButton btnAgregar = new JButton("Agregar Detalle");
        formPanel.add(btnAgregar);
        formPanel.add(new JLabel()); // espacio

        // Tabla
        JScrollPane scrollPane = new JScrollPane(table);

        // Botonera inferior
        JButton btnAprobar = new JButton("Aprobar Transformación");
        JButton btnEjecutar = new JButton("Ejecutar Transformación");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnAprobar);
        bottomPanel.add(btnEjecutar);

        // Agregar todo a la ventana
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarDetalle());
        btnAprobar.addActionListener(e -> aprobarTransformacion());
        btnEjecutar.addActionListener(e -> ejecutarTransformacion());
    }

    private void agregarDetalle() {
        String tipo = cbTipoTransformacion.getSelectedItem().toString();
        String prodOrigen = txtProductoOrigen.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String prodResultado = txtProductoResultado.getText().trim();
        String cantidad = txtCantidadResultado.getText().trim();

        if (prodOrigen.isEmpty() || prodResultado.isEmpty() || cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completá todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{tipo, prodOrigen, ubicacion, prodResultado, cantidad});

        // Limpiar
        txtProductoOrigen.setText("");
        txtUbicacion.setText("");
        txtProductoResultado.setText("");
        txtCantidadResultado.setText("");
    }

    private void aprobarTransformacion() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay detalles cargados para aprobar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        estadoActual = EstadoOrdenTransformacion.APROBADO;
        JOptionPane.showMessageDialog(this, "Orden de transformación aprobada.");
    }

    private void ejecutarTransformacion() {
        if (estadoActual != EstadoOrdenTransformacion.APROBADO) {
            JOptionPane.showMessageDialog(this, "Solo se puede ejecutar una orden aprobada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        estadoActual = EstadoOrdenTransformacion.EJECUTADO;
        JOptionPane.showMessageDialog(this, "Transformación ejecutada (simulado).");
    }
}
