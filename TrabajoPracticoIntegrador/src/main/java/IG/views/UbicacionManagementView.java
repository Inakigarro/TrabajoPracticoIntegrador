package main.java.IG.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UbicacionManagementView extends JFrame {

    private final JComboBox<String> cmbNave = new JComboBox<>(new String[]{"Nave 1", "Nave 2", "Nave 3"}); //Suponiendo que se crearon en la BDD
    private final JTextField txtZona = new JTextField();
    private final JTextField txtEstanteria = new JTextField();
    private final JTextField txtNivel = new JTextField();
    private final JTextField txtCapacidad = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Nave", "Zona", "Estantería", "Nivel", "Capacidad"}, 0);
    private final JTable table = new JTable(tableModel);

    public UbicacionManagementView() {
        setTitle("Gestión de Ubicaciones");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Alta de Ubicación"));

        formPanel.add(new JLabel("Nave:"));
        formPanel.add(cmbNave);
        formPanel.add(new JLabel("Zona:"));
        formPanel.add(txtZona);
        formPanel.add(new JLabel("Estantería:"));
        formPanel.add(txtEstanteria);
        formPanel.add(new JLabel("Nivel:"));
        formPanel.add(txtNivel);
        formPanel.add(new JLabel("Capacidad (kg):"));
        formPanel.add(txtCapacidad);

        JButton btnAgregar = new JButton("Agregar Ubicación");
        JButton btnEliminar = new JButton("Eliminar Seleccionada");
        JButton btnModificar = new JButton("Modificar Seleccionada");

        formPanel.add(btnAgregar);
        formPanel.add(btnModificar);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnEliminar);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarUbicacion());
        btnEliminar.addActionListener(e -> eliminarUbicacion());
        btnModificar.addActionListener(e -> modificarUbicacion());
        table.getSelectionModel().addListSelectionListener(e -> cargarDesdeTabla());
    }

    private void agregarUbicacion() {
        try {
            String nave = (String) cmbNave.getSelectedItem();
            String zona = txtZona.getText().trim();
            int estanteria = Integer.parseInt(txtEstanteria.getText().trim());
            int nivel = Integer.parseInt(txtNivel.getText().trim());
            double capacidad = Double.parseDouble(txtCapacidad.getText().trim());

            if (capacidad > 1250) {
                JOptionPane.showMessageDialog(this, "La capacidad máxima por ubicación es 1250kg.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{nave, zona, estanteria, nivel, capacidad});
            limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verificá que los campos Estantería, Nivel y Capacidad sean numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUbicacion() {
        int fila = table.getSelectedRow();
        if (fila != -1) {
            tableModel.removeRow(fila);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccioná una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarUbicacion() {
        int fila = table.getSelectedRow();
        if (fila != -1) {
            try {
                String nave = (String) cmbNave.getSelectedItem();
                String zona = txtZona.getText().trim();
                int estanteria = Integer.parseInt(txtEstanteria.getText().trim());
                int nivel = Integer.parseInt(txtNivel.getText().trim());
                double capacidad = Double.parseDouble(txtCapacidad.getText().trim());

                if (capacidad > 1250) {
                    JOptionPane.showMessageDialog(this, "La capacidad máxima por ubicación es 1250kg.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tableModel.setValueAt(nave, fila, 0);
                tableModel.setValueAt(zona, fila, 1);
                tableModel.setValueAt(estanteria, fila, 2);
                tableModel.setValueAt(nivel, fila, 3);
                tableModel.setValueAt(capacidad, fila, 4);
                limpiarCampos();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Campos inválidos. Asegurate de ingresar números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccioná una ubicación para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarDesdeTabla() {
        int fila = table.getSelectedRow();
        if (fila != -1) {
            cmbNave.setSelectedItem(tableModel.getValueAt(fila, 0));
            txtZona.setText(tableModel.getValueAt(fila, 1).toString());
            txtEstanteria.setText(tableModel.getValueAt(fila, 2).toString());
            txtNivel.setText(tableModel.getValueAt(fila, 3).toString());
            txtCapacidad.setText(tableModel.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarCampos() {
        txtZona.setText("");
        txtEstanteria.setText("");
        txtNivel.setText("");
        txtCapacidad.setText("");
        table.clearSelection();
    }
}
