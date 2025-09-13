package IG.views;

import IG.application.Dtos.ProductoDto;
import IG.application.Dtos.TipoProductoDto;
import IG.application.interfaces.IServicioProductos;
import IG.application.ServicioProductosDao;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementView extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtUnidad = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JTextField txtCantidad = new JTextField();

    private final JTextField txtTipoProductoDescripcion= new JTextField();

    private final ProductoTableModel tableModel = new ProductoTableModel();
    private final JTable table = new JTable(tableModel);

    private final List<ProductoDto> productos = new ArrayList<>();
    private IServicioProductos servicio;

    public ProductManagementView() {
        this.servicio = new ServicioProductosDao();
        setTitle("Gestión de Productos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(txtDescripcion);
        formPanel.add(new JLabel("Unidad de medida:"));
        formPanel.add(txtUnidad);
        formPanel.add(new JLabel("Cantidad por unidad:"));
        formPanel.add(txtCantidad);
        formPanel.add(new JLabel("Stock:"));
        formPanel.add(txtStock);
        formPanel.add(new JLabel("TipoProducto:"));
        formPanel.add(txtTipoProductoDescripcion);


        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");

        formPanel.add(btnAgregar);
        formPanel.add(btnEliminar);

        try {
            var productos = this.servicio.obtenerProductos();
            productos.forEach(producto -> {
                tableModel.addRow(new Object[]{
                        producto.id(),
                        producto.descripcion(),
                        producto.cantidadUnidad(),
                        producto.unidadMedida(),
                        producto.stock(),
                        producto.tipoProducto()});
            });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de SQL", JOptionPane.ERROR_MESSAGE);
        }
        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
    }

    private void agregarProducto() {
        try {
            String descripcion = txtDescripcion.getText().trim();
            Double cantidad = Double.parseDouble(txtCantidad.getText().trim());
            String unidad = txtUnidad.getText().trim();
            Double stock = Double.parseDouble(txtStock.getText().trim());
            String tipoProductoDesc = txtTipoProductoDescripcion.getText().trim();
            TipoProductoDto tipoProducto = new TipoProductoDto(0,tipoProductoDesc);

            ProductoDto nuevo = new ProductoDto(0, descripcion, cantidad, unidad, stock, tipoProducto);
            var producto = this.servicio.crearProducto(nuevo);
            productos.add(nuevo);
            tableModel.addRow(new Object[]{
                    producto.id(),
                    producto.descripcion(),
                    producto.cantidadUnidad(),
                    producto.unidadMedida(),
                    producto.stock(),
                    producto.tipoProducto()});

            txtId.setText("");
            txtDescripcion.setText("");
            txtUnidad.setText("");
            txtStock.setText("");
            txtCantidad.setText("");
            txtTipoProductoDescripcion.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "ID y Stock deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de SQL", JOptionPane.ERROR_MESSAGE);
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
}

