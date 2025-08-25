package main.java.IG.views;

import main.java.IG.domain.Enums.EstadosOrdenes;
import main.java.IG.domain.Enums.TipoTransformacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrdenTransformacionView extends JFrame {

    private final JComboBox<TipoTransformacion> cbTipoTransformacion =
            new JComboBox<>(TipoTransformacion.values());
    private final JTextField txtProductoOrigen = new JTextField();
    private final JTextField txtUbicacion = new JTextField();
    private final JTextField txtProductoResultado = new JTextField();
    private final JTextField txtCantidadResultado = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Tipo", "Prod. Origen", "Ubicación", "Prod. Resultado", "Cantidad"}, 0);
    private final JTable table = new JTable(tableModel);

    private EstadosOrdenes estadoActual = EstadosOrdenes.PENDIENTE;

    public OrdenTransformacionView() {
        setTitle("Órdenes de Transformación");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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
        formPanel.add(new JLabel());

        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAprobar = new JButton("Aprobar Transformación");
        JButton btnEjecutar = new JButton("Ejecutar Transformación");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnAprobar);
        bottomPanel.add(btnEjecutar);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnAprobar.addActionListener(e -> aprobarTransformacion());
        btnEjecutar.addActionListener(e -> ejecutarTransformacion());
    }

    private void agregarDetalle() {
        TipoTransformacion tipo = (TipoTransformacion) cbTipoTransformacion.getSelectedItem();
        String prodOrigen = txtProductoOrigen.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String prodResultado = txtProductoResultado.getText().trim();
        String cantidad = txtCantidadResultado.getText().trim();

        if (prodOrigen.isEmpty() || prodResultado.isEmpty() || cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completá todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{
                tipo.getDescripcion(),
                prodOrigen,
                ubicacion,
                prodResultado,
                cantidad
        });

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

        estadoActual = EstadosOrdenes.APROBADO;
        JOptionPane.showMessageDialog(this, "Orden de transformación aprobada.");
    }

    private void ejecutarTransformacion() {
        if (estadoActual != EstadosOrdenes.APROBADO) {
            JOptionPane.showMessageDialog(this, "Solo se puede ejecutar una orden aprobada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        estadoActual = EstadosOrdenes.PROCESO;
        JOptionPane.showMessageDialog(this, "Transformación ejecutada (simulado).");
    }
}

