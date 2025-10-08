package IG.views;

import IG.application.Dtos.Ubicacion.UbicacionCache;
import IG.application.ServicioProductosDao;
import IG.application.interfaces.IServicioOrdenMovimiento;
import IG.application.interfaces.IServicioProductos;
import IG.application.utils.Converter;
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
    private JLabel lblCmbUbicacionDestino;
    private JTable tablaDetalles;
    private JComboBox<ProductoDto> cmbProducto;
    private JComboBox<UbicacionDto> cmbUbicacion;
    private JComboBox<UbicacionDto> cmbUbicacionDestino;
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
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información de la Orden"));
        add(panelInfo, BorderLayout.NORTH);

        // Inicialización de componentes antes de agregarlos al panel
        lblCmbProducto = new JLabel("Producto:");
        cmbProducto = new JComboBox<>();
        lblCmbUbicacion = new JLabel("Ubicación:");
        cmbUbicacion = new JComboBox<>();
        lblCmbUbicacionDestino = new JLabel("Ubicación Destino:");
        cmbUbicacionDestino = new JComboBox<>();
        txtCantidad = new JTextField(6);
        chkSalida = new JCheckBox("Salida", true);
        btnAgregarDetalle = new JButton("Agregar Detalle");
        btnAgregarDetalle.addActionListener((a) -> agregarDetalle());
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(a -> guardarDetalles());

        // Panel superior con dos columnas y separador vertical
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Agregar Detalle"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Columna 1: Ubicaciones
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSuperior.add(lblCmbUbicacion, gbc);
        gbc.gridy++;
        panelSuperior.add(cmbUbicacion, gbc);
        gbc.gridy++;
        panelSuperior.add(lblCmbUbicacionDestino, gbc);
        gbc.gridy++;
        panelSuperior.add(cmbUbicacionDestino, gbc);

        // Separador vertical
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        JSeparator sepVertical = new JSeparator(SwingConstants.VERTICAL);
        sepVertical.setPreferredSize(new Dimension(2, 120));
        panelSuperior.add(sepVertical, gbc);
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Columna 2: Producto y cantidad
        gbc.gridx = 2;
        gbc.gridy = 0;
        panelSuperior.add(lblCmbProducto, gbc);
        gbc.gridy++;
        panelSuperior.add(cmbProducto, gbc);
        gbc.gridy++;
        panelSuperior.add(new JLabel("Cantidad:"), gbc);
        gbc.gridy++;
        panelSuperior.add(txtCantidad, gbc);
        gbc.gridy++;
        panelSuperior.add(chkSalida, gbc);
        gbc.gridy++;
        panelSuperior.add(btnAgregarDetalle, gbc);
        gbc.gridy++;
        panelSuperior.add(btnGuardar, gbc);

        // Panel central con separador horizontal
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentral.add(panelSuperior);
        panelCentral.add(Box.createVerticalStrut(10));
        JSeparator sepHorizontal = new JSeparator(SwingConstants.HORIZONTAL);
        sepHorizontal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        panelCentral.add(sepHorizontal);
        panelCentral.add(Box.createVerticalStrut(10));
        tablaDetalles = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden"));
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
                    lblCmbUbicacionDestino.setVisible(false);
                    cmbUbicacionDestino.setVisible(false);
                }
                case INTERNO -> {
                    lblCmbUbicacion.setVisible(true);
                    cmbUbicacion.setVisible(true);
                    lblCmbUbicacionDestino.setVisible(true);
                    cmbUbicacionDestino.setVisible(true);
                    // Poblar ubicaciones destino
                    cmbUbicacionDestino.removeAllItems();
                    var ubicaciones = servicioUbicaciones.listarUbicaciones();
                    if (ubicaciones != null) {
                        for (var u : ubicaciones) cmbUbicacionDestino.addItem(u);
                    }
                }
                default -> {
                    cmbUbicacion.setVisible(true);
                    lblCmbUbicacionDestino.setVisible(false);
                    cmbUbicacionDestino.setVisible(false);
                }
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
                        // Cualico la cantidad de producto en esa ubicacion.
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
                    // Busco la ubicacion origen
                    var ubicacionOrigenSeleccionada = (UbicacionDto) cmbUbicacion.getSelectedItem();
                    if (ubicacionOrigenSeleccionada == null) {
                        JOptionPane.showMessageDialog(this, "Debe seleccionar una ubicación de origen.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    var ubicacionOrigen = this.ubicaciones.stream()
                            .filter(ub -> ub.id.equals(ubicacionOrigenSeleccionada.id()))
                            .findFirst().orElse(null);
                    if (ubicacionOrigen == null || !ubicacionOrigen.productos.containsKey(producto.id())) {
                        JOptionPane.showMessageDialog(this, "La ubicación de origen no contiene el producto seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Busco la ubicacion destino
                    var ubicacionDestinoSeleccionada = (UbicacionDto) cmbUbicacionDestino.getSelectedItem();
                    if (ubicacionDestinoSeleccionada == null) {
                        JOptionPane.showMessageDialog(this, "Debe seleccionar una ubicación de destino.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    var ubicacionDestino = this.ubicaciones.stream()
                            .filter(ub -> ub.id.equals(ubicacionDestinoSeleccionada.id()))
                            .findFirst().orElse(null);
                    if (ubicacionDestino == null) {
                        JOptionPane.showMessageDialog(this, "La ubicación de destino no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Verifico que la ubicacion origen tenga suficiente stock del producto.
                    double cantidadEnOrigen = ubicacionOrigen.productos.get(producto.id());
                    if (cantidadEnOrigen < cantidadPeso) {
                        JOptionPane.showMessageDialog(this, "La ubicación de origen no tiene suficiente stock del producto seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Si la ubicacion origen y destino son iguales, no tiene sentido mover el producto.
                    if (ubicacionOrigen.id.equals(ubicacionDestino.id)) {
                        JOptionPane.showMessageDialog(this, "La ubicación de origen y destino no pueden ser las mismas.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Si la ubicacion destino tiene espacio disponible, genero el detalle de salida y el de ingreso.
                    if (ubicacionDestino.tieneCapacidadDisponible(cantidadPeso)) {
                        detalles.add(new DetalleMovimientoDto(0, cantidadPeso, producto, UbicacionDto.map(ubicacionOrigen), true));
                        detalles.add(new DetalleMovimientoDto(0, cantidadPeso, producto, UbicacionDto.map(ubicacionDestino), false));
                        // Actualizo las ubicaciones en caché.
                        ubicacionOrigen.capacidadUsada -= cantidadPeso;
                        ubicacionOrigen.productos.put(producto.id(), cantidadEnOrigen - cantidadPeso);

                        ubicacionDestino.capacidadUsada += cantidadPeso;
                        ubicacionOrigen.productos.put(producto.id(), cantidadEnOrigen + cantidadPeso);
                        cargarTablaDetalles();
                    } else {
                        JOptionPane.showMessageDialog(this, "La ubicación de destino no tiene suficiente capacidad para la cantidad especificada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
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
