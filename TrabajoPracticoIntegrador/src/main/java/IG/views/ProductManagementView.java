package IG.views;

import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Producto.TipoProductoDto;
import IG.application.interfaces.IServicioProductos;
import IG.application.ServicioProductosDao;
import IG.domain.Enums.ProductoUnidades;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementView extends JFrame {

    private final JTextField txtId = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JComboBox<ProductoUnidades> cmbUnidad = new JComboBox(ProductoUnidades.values());
    private final JTextField txtCantidad = new JTextField();

    private final JComboBox<TipoProductoDto> cmbTipoProducto = new JComboBox<>();
    private final JButton btnAgregarTipoProducto = new JButton("Agregar Tipo de Producto");

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
        formPanel.add(cmbUnidad);
        formPanel.add(new JLabel("Cantidad por unidad:"));
        formPanel.add(txtCantidad);

        try {
            var tipoProductos = this.servicio.obtenerTiposProductos();
            tipoProductos.forEach(cmbTipoProducto::addItem);
            JPanel tipoProductoPanel = new JPanel(new BorderLayout());
            tipoProductoPanel.add(cmbTipoProducto, BorderLayout.CENTER);
            tipoProductoPanel.add(btnAgregarTipoProducto, BorderLayout.EAST);
            formPanel.add(new JLabel("TipoProducto:"));
            formPanel.add(tipoProductoPanel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de SQL", JOptionPane.ERROR_MESSAGE);
        }


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
        btnAgregarTipoProducto.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Agregar Tipo de Producto", true);
            TipoProductoManagementView tipoProductoView = new TipoProductoManagementView();
            dialog.setContentPane(tipoProductoView.getContentPane());
            dialog.setSize(tipoProductoView.getSize());
            dialog.setLocationRelativeTo(this);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    recargarComboTipoProducto();
                }
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    recargarComboTipoProducto();
                }
            });
            dialog.setVisible(true);
        });
    }

    private void agregarProducto() {
        try {
            String descripcion = txtDescripcion.getText().trim();
            Double cantidad = Double.parseDouble(txtCantidad.getText().trim());
            ProductoUnidades unidad = (ProductoUnidades) cmbUnidad.getSelectedItem();
            TipoProductoDto tipoProducto = (TipoProductoDto) cmbTipoProducto.getSelectedItem();

            ProductoDto nuevo = new ProductoDto(0, descripcion, cantidad, unidad, 0d, tipoProducto);
            var producto = this.servicio.crearProducto(nuevo);
            productos.add(nuevo);
            tableModel.addRow(new Object[]{
                    producto.id(),
                    producto.descripcion(),
                    producto.cantidadUnidad(),
                    producto.unidadMedida().getDescripcion(),
                    producto.stock(),
                    producto.tipoProducto()});

            txtId.setText("");
            txtDescripcion.setText("");
            cmbUnidad.setSelectedIndex(0);
            txtCantidad.setText("");
            cmbTipoProducto.setSelectedIndex(0);

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

    private void recargarComboTipoProducto() {
        cmbTipoProducto.removeAllItems();
        try {
            var tipoProductos = this.servicio.obtenerTiposProductos();
            tipoProductos.forEach(cmbTipoProducto::addItem);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}
