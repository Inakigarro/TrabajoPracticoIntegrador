package main.java.IG.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UbicacionManagementView extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JTextField txtZona = new JTextField();
    private final JTextField txtCapacidad = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Zona", "Capacidad"}, 0);
    private final JTable table = new JTable(tableModel);

    private final List<String[]> ubicaciones = new ArrayList<>(); // temporal

    public UbicacionManagementView() {
        setTitle("Gestión de Ubicaciones");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Zona:"));
        formPanel.add(txtZona);
        formPanel.add(new JLabel("Capacidad (kg):"));
        formPanel.add(txtCapacidad);

        JButton btnAgregar = new JButton("Agregar Ubicación");
        formPanel.add(btnAgregar);

        JScrollPane scrollPane = new JScrollPane(table);
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarUbicacion());
    }

    private void agregarUbicacion() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String zona = txtZona.getText().trim();
            double capacidad = Double.parseDouble(txtCapacidad.getText().trim());

            if (capacidad > 1250) {
                JOptionPane.showMessageDialog(this, "La capacidad máxima es 1250kg.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ubicaciones.add(new String[]{String.valueOf(id), zona, String.valueOf(capacidad)});
            tableModel.addRow(new Object[]{id, zona, capacidad});

            txtId.setText("");
            txtZona.setText("");
            txtCapacidad.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Campos numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
