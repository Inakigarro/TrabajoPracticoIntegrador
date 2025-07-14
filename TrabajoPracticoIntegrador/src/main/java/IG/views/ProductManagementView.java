package main.java.IG.views;

import main.java.IG.domain.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementView extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtUnidad = new JTextField();
    private final JTextField txtStock = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Descripción", "Unidad", "Stock"}, 0);
    private final JTable table = new JTable(tableModel);

    private final List<Producto> productos = new ArrayList<>();

    public ProductManagementView() {
        setTitle("Gestión de Productos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(txtDescripcion);
        formPanel.add(new JLabel("Unidad de medida:"));
        formPanel.add(txtUnidad);
        formPanel.add(new JLabel("Stock:"));
        formPanel.add(txtStock);

        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        //JButton btnGuardar = new JButton("Guardar en archivo");

        formPanel.add(btnAgregar);
        formPanel.add(btnEliminar);
        //formPanel.add(btnGuardar);

        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        //btnGuardar.addActionListener(e -> guardarProductosEnArchivo());
    }

    private void agregarProducto() {
        try {
            Integer id = Integer.parseInt(txtId.getText().trim());
            String descripcion = txtDescripcion.getText().trim();
            String unidad = txtUnidad.getText().trim();
            Double stock = Double.parseDouble(txtStock.getText().trim());

            Producto nuevo = new Producto(id, descripcion, unidad, stock);
            productos.add(nuevo);
            tableModel.addRow(new Object[]{id, descripcion, unidad, stock});

            txtId.setText("");
            txtDescripcion.setText("");
            txtUnidad.setText("");
            txtStock.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "ID y Stock deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int fila = table.getSelectedRow();
        if (fila != -1) {
            productos.remove(fila);
            tableModel.removeRow(fila);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccioná una fila para eliminar.");
        }
    }

    /*private void guardarProductosEnArchivo() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("productos.txt");

            for (Producto p : productos) {
                String linea = p.getId() + ";" + p.getDescripcion() + ";" + p.getUnidadMedida() + ";" + p.getStock();
                writer.write(linea + "\n");
            }

            writer.close();
            JOptionPane.showMessageDialog(this, "Productos guardados en productos.txt");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/

    /*El metodo guardar producto, esta pensado para pasar el producto agregado a un txt o un json para
    luego persistirlo en la base de datos. Si no es necesario; Borrar.*/

}
