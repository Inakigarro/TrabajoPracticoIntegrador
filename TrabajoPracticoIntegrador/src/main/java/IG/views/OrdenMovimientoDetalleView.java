package IG.views;

import IG.application.Dtos.Producto.TipoProductoDto;
import IG.application.ServicioProductosDao;
import IG.application.interfaces.IServicioOrdenMovimiento;
import IG.application.interfaces.IServicioProductos;
import IG.domain.Clases.Producto;
import IG.domain.Clases.Ubicacion;
import IG.domain.Enums.TipoMovimiento;
import IG.application.Dtos.OrdenMovimiento.OrdenMovimientoDto;
import IG.application.Dtos.OrdenMovimiento.DetalleMovimientoDto;
import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdenMovimientoDetalleView extends JFrame {
    private final IServicioOrdenMovimiento servicio;
    private final int ordenId;
    private OrdenMovimientoDto orden;
    private List<DetalleMovimientoDto> detalles;

    private JLabel lblTipo;
    private JLabel lblFecha;
    private JLabel lblEstado;
    private JLabel lblCmbProducto;
    private JLabel lblCmbUbicacion;
    private JTable tablaDetalles;
    private JComboBox<ProductoDto> cmbProducto;
    private JComboBox<UbicacionDto> cmbUbicacion;
    private JTextField txtCantidad;
    private JCheckBox chkSalida;
    private JButton btnAgregarDetalle;
    private JButton btnGuardar;
    private IServicioProductos servicioProductos = new ServicioProductosDao();
    private IG.application.ServicioUbicaciones servicioUbicaciones = new IG.application.ServicioUbicaciones();

    public OrdenMovimientoDetalleView(IServicioOrdenMovimiento servicio, int ordenId) {
        this.servicio = servicio;
        this.ordenId = ordenId;
        setTitle("Detalle de Orden de Movimiento");
        setSize(700, 500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        cargarDatos();
        cargarCombos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 10, 10));
        lblTipo = new JLabel();
        lblFecha = new JLabel();
        lblEstado = new JLabel();
        panelInfo.add(new JLabel("Tipo:"));
        panelInfo.add(lblTipo);
        panelInfo.add(new JLabel("Fecha:"));
        panelInfo.add(lblFecha);
        panelInfo.add(new JLabel("Estado:"));
        panelInfo.add(lblEstado);
        add(panelInfo, BorderLayout.NORTH);

        // Panel para agregar detalle
        cmbProducto = new JComboBox<>();
        lblCmbProducto = new JLabel("Producto:");
        cmbUbicacion = new JComboBox<>();
        lblCmbUbicacion = new JLabel("Ubicacion:");
        txtCantidad = new JTextField(6);
        chkSalida = new JCheckBox("Salida", true);
        btnAgregarDetalle = new JButton("Agregar Detalle");
        btnGuardar = new JButton("Guardar");
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelAgregar.add(new JLabel("Producto:"));
        panelAgregar.add(cmbProducto);
        panelAgregar.add(new JLabel("Ubicación:"));
        panelAgregar.add(cmbUbicacion);
        panelAgregar.add(new JLabel("Cantidad:"));
        panelAgregar.add(txtCantidad);
        panelAgregar.add(chkSalida);
        panelAgregar.add(btnAgregarDetalle);
        panelAgregar.add(btnGuardar);
        btnAgregarDetalle.addActionListener(e -> agregarDetalle());
        btnGuardar.addActionListener(e -> guardarDetalles());
        // Ajustar tamaño máximo para que ocupe solo una fila
        panelAgregar.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelAgregar.getPreferredSize().height));
        cmbProducto.setMaximumSize(new Dimension(120, 25));
        cmbUbicacion.setMaximumSize(new Dimension(120, 25));
        txtCantidad.setMaximumSize(new Dimension(60, 25));
        btnAgregarDetalle.setMaximumSize(new Dimension(140, 25));
        chkSalida.setMaximumSize(new Dimension(80, 25));
        // Panel intermedio con BoxLayout vertical
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.add(panelAgregar);
        tablaDetalles = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        panelCentral.add(scrollPane);
        add(panelCentral, BorderLayout.CENTER);
    }

    private void cargarDatos() {
        try {
            orden = servicio.buscarPorId(ordenId);
            detalles = servicio.listarDetallesPorOrden(ordenId);
            lblTipo.setText(orden.tipo().getDescripcion());
            lblFecha.setText(orden.fecha().toString());
            lblEstado.setText(orden.estado().getDescripcion());
            cargarTablaDetalles();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaDetalles() {
        String[] columnas = {"Producto", "Cantidad", "Ubicación", "Salida"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        for (DetalleMovimientoDto dm : detalles) {
            Object[] fila = {
                dm.producto() != null ? dm.producto().descripcion() : "",
                dm.cantidad(),
                dm.ubicacion() != null ? dm.ubicacion().toString() : "",
                dm.esSalida() ? "Sí" : "No"
            };
            modelo.addRow(fila);
        }
        tablaDetalles.setModel(modelo);
    }

    private void cargarCombos() {
        try {
            var productos = servicioProductos.obtenerProductos();
            cmbProducto.removeAllItems();
            if (productos != null) {
                for (var p : productos) cmbProducto.addItem(p);
            }
            cmbProducto.addActionListener(e -> actualizarUbicacionesPorProducto());
            actualizarUbicacionesPorProducto();
            switch (orden.tipo()) {
                case INGRESO -> {
                    cmbUbicacion.setVisible(false);
                }
                case INTERNO -> {
                    cmbUbicacion.setVisible(true);
                }
                case EGRESO -> {
                    cmbUbicacion.setVisible(false);
                }
                default -> cmbUbicacion.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos/ubicaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarUbicacionesPorProducto() {
        var producto = (ProductoDto) cmbProducto.getSelectedItem();
        cmbUbicacion.removeAllItems();
        if (orden.tipo() == TipoMovimiento.INTERNO && producto != null) {
            var ubicaciones = servicioUbicaciones.listarUbicacionesPorProducto(producto.id());
            for (var u : ubicaciones) {
                cmbUbicacion.addItem(u);
            }
        } else {
            var ubicaciones = servicioUbicaciones.listarUbicaciones();
            if (ubicaciones != null) {
                for (var u : ubicaciones) cmbUbicacion.addItem(u);
            }
        }
    }

    private void agregarDetalle() {
        try {
            ProductoDto producto = null;
            UbicacionDto ubicacion = null;
            Object prodObj = cmbProducto.getSelectedItem();
            Object ubiObj = cmbUbicacion.getSelectedItem();
            if (prodObj instanceof ProductoDto) producto = (ProductoDto) prodObj;
            else if (prodObj instanceof Producto) {
                Producto p = (Producto) prodObj;
                producto = new ProductoDto(p.getId(), p.getDescripcion(), p.getCantidadUnidad(), p.getUnidadMedida(), p.getStock(), TipoProductoDto.map(p.getTipoProducto()));
            }
            if (ubiObj instanceof UbicacionDto) ubicacion = (UbicacionDto) ubiObj;
            else if (ubiObj instanceof Ubicacion) {
                Ubicacion u = (Ubicacion) ubiObj;
                ubicacion = UbicacionDto.map(u);
            }
            double cantidad = Double.parseDouble(txtCantidad.getText());
            boolean esSalida = chkSalida.isSelected();
            if (producto == null || (cmbUbicacion.isVisible() && ubicacion == null) || cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DetalleMovimientoDto detalleDto = new DetalleMovimientoDto(0, cantidad, producto, esSalida, ubicacion);
            detalles.add(detalleDto);
            cargarTablaDetalles();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar detalle: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarDetalles() {
        try {
            if (detalles == null || detalles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay detalles para guardar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            servicio.actualizarDetallesOrdenMovimiento(this.ordenId, this.detalles);
            JOptionPane.showMessageDialog(this, "Detalles guardados correctamente.");
            cargarDatos(); // Recarga desde base de datos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
