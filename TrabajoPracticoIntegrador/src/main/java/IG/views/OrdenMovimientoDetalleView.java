package IG.views;

import IG.application.Dtos.Producto.ProductoCache;
import IG.application.Dtos.Producto.TipoProductoDto;
import IG.application.Dtos.ProductoUbicacionCache;
import IG.application.Dtos.ProductoUbicacionDto;
import IG.application.Dtos.Ubicacion.UbicacionCache;
import IG.application.ServicioProductosDao;
import IG.application.interfaces.IServicioOrdenMovimiento;
import IG.application.interfaces.IServicioProductos;
import IG.application.utils.Converter;
import IG.domain.Clases.Producto;
import IG.domain.Clases.Ubicacion;
import IG.domain.Constants.UbicacionConstants;
import IG.domain.Enums.TipoMovimiento;
import IG.application.Dtos.OrdenMovimiento.OrdenMovimientoDto;
import IG.application.Dtos.OrdenMovimiento.DetalleMovimientoDto;
import IG.application.Dtos.Producto.ProductoDto;
import IG.application.Dtos.Ubicacion.UbicacionDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenMovimientoDetalleView extends JFrame {
    private final IServicioOrdenMovimiento servicio;
    private List<UbicacionCache> ubicaciones = new ArrayList<>();

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
        panelAgregar.add(lblCmbProducto);
        panelAgregar.add(cmbProducto);
        panelAgregar.add(lblCmbUbicacion);
        panelAgregar.add(cmbUbicacion);
        panelAgregar.add(new JLabel("Cantidad:"));
        panelAgregar.add(txtCantidad);
        panelAgregar.add(chkSalida);
        panelAgregar.add(btnAgregarDetalle);
        panelAgregar.add(btnGuardar);
        btnAgregarDetalle.addActionListener(e -> agregarDetalle());
        btnGuardar.addActionListener(e -> {
            guardarDetalles();
        });
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
            this.ubicaciones = servicioUbicaciones.obtenerTodasLasUbicaciones().stream().map(UbicacionCache::map).toList();
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
                case INGRESO, EGRESO -> {
                    lblCmbUbicacion.setVisible(false);
                    cmbUbicacion.setVisible(false);
                }
                case INTERNO -> {
                    lblCmbUbicacion.setVisible(true);
                    cmbUbicacion.setVisible(true);
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
            Double cantidad = Double.parseDouble(txtCantidad.getText());
            ProductoDto producto = (ProductoDto) cmbProducto.getSelectedItem();
            if (producto == null || cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Double cantidadPeso = calcularCantidadPeso(producto, cantidad);

            switch (orden.tipo()) {
                case INGRESO: {
                    // Buscar ubicaciones disponibles para la cantidad en peso
                    var ubicacionesDisponibles = this.filtrarUbicacionesDisponibles(this.ubicaciones, cantidadPeso);

                    // Busco ubicaciones disponibles que ya tengan el producto seleccionado y las ordeno por capacidad usada.
                    var ubicacionesConProducto = this.filtrarUbicacionesConProducto(ubicacionesDisponibles, producto.id());

                    // Mientras haya cantidad por ubicar y haya ubicaciones disponibles
                    for (var ubicacion : ubicacionesConProducto) {
                        if (cantidadPeso <= 0) break;
                        double espacioDisponible = UbicacionConstants.UBICACION_CAPACIDAD_MAX - ubicacion.capacidadUsada;
                        if (espacioDisponible <= 0) continue; // Si no hay espacio, sigo con la siguiente
                        double cantidadAUbicar = Math.min(cantidadPeso, espacioDisponible);
                        if (ubicacion.capacidadUsada + cantidadAUbicar < UbicacionConstants.UBICACION_CAPACIDAD_MAX) {
                            ubicacion.productos.put(producto.id(), cantidadAUbicar);
                        }
                        DetalleMovimientoDto detalleDto = new DetalleMovimientoDto(0, cantidadAUbicar, producto, UbicacionDto.map(ubicacion), false);
                        detalles.add(detalleDto);
                        cantidadPeso -= cantidadAUbicar;
                    }

                    // Si queda cantidad por ubicar, busco en las ubicaciones vacías
                    if (cantidadPeso > 0) {
                        // Busco en las ubicaciones disponibles, que no contengan el producto y que tengan espacio disponible.
                        Double finalCantidadPeso = cantidadPeso;
                        var ubicacionesDisponiblesSinProducto = ubicacionesDisponibles.stream()
                                .filter(ub -> !ubicacionesConProducto.contains(ub) && (ub.capacidadUsada - UbicacionConstants.UBICACION_CAPACIDAD_MAX) > finalCantidadPeso)
                                .toList();

                        for (var ubicacion : ubicacionesDisponiblesSinProducto) {
                            if (cantidadPeso <= 0) break;
                            double espacioDisponible = UbicacionConstants.UBICACION_CAPACIDAD_MAX;
                            double cantidadAUbicar = Math.min(cantidadPeso, espacioDisponible);
                            if (ubicacion.capacidadUsada + cantidadAUbicar < UbicacionConstants.UBICACION_CAPACIDAD_MAX) {
                                ubicacion.productos.put(producto.id(), cantidadAUbicar);
                            }
                            DetalleMovimientoDto detalleDto = new DetalleMovimientoDto(0, cantidadAUbicar, producto, UbicacionDto.map(ubicacion), false);
                            detalles.add(detalleDto);
                            cantidadPeso -= cantidadAUbicar;
                        }

                        // Si queda cantidad por ubicar, busco en las ubicaciones restantes.
                        if (cantidadPeso <= 0) break;

                        var ubicacionesRestantes = ubicacionesDisponibles.stream()
                                .filter(ub -> !ubicacionesConProducto.contains(ub) && !ubicacionesDisponiblesSinProducto.contains(ub))
                                .toList();

                        for (var ubicacion : ubicacionesRestantes) {
                            if (cantidadPeso <= 0) break;
                            double espacioDisponible = UbicacionConstants.UBICACION_CAPACIDAD_MAX;
                            double cantidadAUbicar = Math.min(cantidadPeso, espacioDisponible);
                            if (ubicacion.capacidadUsada + cantidadAUbicar < UbicacionConstants.UBICACION_CAPACIDAD_MAX) {
                                ubicacion.productos.put(producto.id(), cantidadAUbicar);
                            }
                            DetalleMovimientoDto detalleDto = new DetalleMovimientoDto(0, cantidadAUbicar, producto, UbicacionDto.map(ubicacion), false);
                            detalles.add(detalleDto);
                            cantidadPeso -= cantidadAUbicar;
                        }
                    }
                    if (cantidadPeso > 0) {
                        JOptionPane.showMessageDialog(this, "No hay suficiente espacio en las ubicaciones para la cantidad especificada. Se agregó la cantidad que pudo ser ubicada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                    cargarTablaDetalles();
                    return;
                }
                case EGRESO: {
                    // Busco todas las ubicaciones con el producto seleccionado.
                    var ubicacionesConProducto = this.filtrarUbicacionesConProducto(this.ubicaciones, producto.id());

                    // Mientras haya cantidad por sacar y haya ubicaciones con el producto
                    for (var ubicacion : ubicacionesConProducto) {
                        if (cantidadPeso <= 0) break;
                        // Cualculo la cantidad de producto en esa ubicacion.
                        double cantidadEnUbicacion = ubicacion.productos.get(producto.id());
                        if (cantidadEnUbicacion <= 0) continue; // Si no hay cantidad, sigo con la siguiente
                        double cantidadASacar = Math.min(cantidadPeso, cantidadEnUbicacion);
                        if (cantidadEnUbicacion - cantidadASacar >= 0){
                            ubicacion.capacidadUsada -= cantidadASacar;
                            ubicacion.productos.put(producto.id(), cantidadEnUbicacion - cantidadASacar);
                        } else {
                            // Si la cantidad a sacar es igual o mayor a la cantidad en la ubicacion, elimino el producto de la ubicacion.
                            ubicacion.capacidadUsada -= cantidadEnUbicacion;
                            ubicacion.productos.remove(producto.id());
                        }
                        DetalleMovimientoDto detalleDto = new DetalleMovimientoDto(0, cantidadASacar, producto, UbicacionDto.map(ubicacion), true);
                        detalles.add(detalleDto);
                        cantidadPeso -= cantidadASacar;
                    }

                    if (cantidadPeso > 0) {
                        JOptionPane.showMessageDialog(this, "No hay suficiente stock en las ubicaciones para la cantidad especificada. Se agregó la cantidad que pudo ser retirada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                    cargarTablaDetalles();
                    return;
                }
                case INTERNO: {
                    // Verifico que haya ubicacion seleccionada.
                    UbicacionDto ubicacion = (UbicacionDto) cmbUbicacion.getSelectedItem();
                    if (ubicacion == null) {
                        JOptionPane.showMessageDialog(this, "Debe seleccionar una ubicación de origen.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Verifico que la ubicacion tenga el producto y la cantidad suficiente.
                    double cantidadEnUbicacion = ubicacion
                            .productos().stream().filter(pu ->
                                    pu.producto().id() == producto.id()).mapToDouble(ProductoUbicacionDto::stockProductoUbicacion).sum();
                    Boolean esSalida = chkSalida.isSelected();
                    if (esSalida) {
                        if (cantidadEnUbicacion < cantidad) {
                            JOptionPane.showMessageDialog(this, "La ubicación seleccionada no tiene suficiente stock del producto.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        double espacioDisponible = UbicacionConstants.UBICACION_CAPACIDAD_MAX - ubicacion.capacidadUsada();
                        if (espacioDisponible < cantidadPeso) {
                            JOptionPane.showMessageDialog(this, "La ubicación seleccionada no tiene suficiente espacio para la cantidad del producto.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    DetalleMovimientoDto detalleDto = new DetalleMovimientoDto(0, cantidad, producto, ubicacion, esSalida);
                    detalles.add(detalleDto);
                }
            }
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
            cargarDatos();
            this.setVisible(false);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Double calcularCantidadPeso(ProductoDto producto, Double cantidad) throws Exception {
        if (producto == null || producto.cantidadUnidad() <= 0) {
            return cantidad;
        }

        switch (producto.unidadMedida()) {
            case KILOGRAMOS -> {
                return cantidad * producto.cantidadUnidad();
            }
            case LITROS -> {
                return cantidad * Converter.litroAKilogramo(producto.cantidadUnidad());
            }
            case GRAMOS -> {
                return cantidad * Converter.gramoAKilogramo(producto.cantidadUnidad());
            }
            case MILILITROS -> {
                return cantidad * Converter.mililitroAKilogramo(producto.cantidadUnidad());
            }
            default -> {
                throw new IllegalArgumentException("Unidad de medida no soportada para conversión a peso.");
            }
        }
    }

    private List<UbicacionCache> filtrarUbicacionesDisponibles(List<UbicacionCache> ubicaciones, Double cantidadPeso) {
        return ubicaciones.stream()
                .filter(ub -> (UbicacionConstants.UBICACION_CAPACIDAD_MAX - ub.capacidadUsada) >= cantidadPeso)
                .toList();
    }
    private List<UbicacionCache> filtrarUbicacionesConProducto(List<UbicacionCache> ubicaciones, Integer productoId) {
        return ubicaciones.stream()
                .filter(ub -> ub.productos.containsKey(productoId))
                .toList();
    }
}
