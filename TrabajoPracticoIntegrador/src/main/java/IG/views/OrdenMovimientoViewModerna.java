package IG.views;

import IG.application.Dtos.OrdenMovimiento.OrdenMovimientoDto;
import IG.application.ServicioOrdenMovimiento;
import IG.application.interfaces.IServicioOrdenMovimiento;
import IG.domain.Clases.OrdenMovimiento;
import IG.domain.DAO.OrdenMovimientoDAO;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoMovimiento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenMovimientoViewModerna extends JFrame {
    private final IServicioOrdenMovimiento servicio;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscarId;
    private JComboBox<TipoMovimiento> cmbTipo;
    private JComboBox<EstadosOrdenes> cmbEstado;
    private JButton btnCrear, btnBuscar, btnModificarEstado, btnActualizar, btnEliminar, btnRefrescar;
    private JTextArea areaDetalle;

    public OrdenMovimientoViewModerna() {
        this.servicio = new ServicioOrdenMovimiento();
        setTitle("Gestión Moderna de Órdenes de Movimiento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // Panel superior: acciones y filtros
        JPanel panelAcciones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtBuscarId = new JTextField(8);
        cmbTipo = new JComboBox<TipoMovimiento>(TipoMovimiento.values());
        cmbEstado = new JComboBox<>(EstadosOrdenes.values());
        btnBuscar = new JButton("Buscar por ID");
        btnCrear = new JButton("Crear Orden");
        btnModificarEstado = new JButton("Modificar Estado");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");

        gbc.gridx = 0; gbc.gridy = 0; panelAcciones.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; panelAcciones.add(txtBuscarId, gbc);
        gbc.gridx = 2; panelAcciones.add(btnBuscar, gbc);
        gbc.gridx = 3; panelAcciones.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 4; panelAcciones.add(cmbTipo, gbc);
        gbc.gridx = 5; panelAcciones.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 6; panelAcciones.add(cmbEstado, gbc);
        gbc.gridx = 7; panelAcciones.add(btnRefrescar, gbc);
        gbc.gridx = 8; panelAcciones.add(btnCrear, gbc);
        gbc.gridx = 9; panelAcciones.add(btnModificarEstado, gbc);
        gbc.gridx = 10; panelAcciones.add(btnActualizar, gbc);
        gbc.gridx = 11; panelAcciones.add(btnEliminar, gbc);

        add(panelAcciones, BorderLayout.NORTH);

        // Panel central: tabla de órdenes
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Tipo", "Fecha", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaOrdenes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaOrdenes);
        add(scrollTabla, BorderLayout.CENTER);

        // Acciones de botones
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnBuscar.addActionListener(this::buscarOrden);
        btnCrear.addActionListener(this::crearOrden);
        btnModificarEstado.addActionListener(this::modificarEstado);
        btnActualizar.addActionListener(this::actualizarOrden);
        btnEliminar.addActionListener(this::eliminarOrden);
        tablaOrdenes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && tablaOrdenes.getSelectedRow() != -1) {
                    int fila = tablaOrdenes.getSelectedRow();
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    OrdenMovimientoDetalleView detalleView = new OrdenMovimientoDetalleView(servicio, id);
                    detalleView.setVisible(true);
                }
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<OrdenMovimientoDto> ordenes = servicio.listarOrdenes(100, 1);
        for (OrdenMovimientoDto o : ordenes) {
            modeloTabla.addRow(new Object[]{o.id(), o.tipo(), o.fecha(), o.estado()});
        }
    }

    private void buscarOrden(ActionEvent e) {
        try {
            int id = Integer.parseInt(txtBuscarId.getText().trim());
            OrdenMovimientoDto orden = servicio.buscarPorId(id);
            if (orden != null) {
                modeloTabla.setRowCount(0);
                modeloTabla.addRow(new Object[]{orden.id(), orden.tipo(), orden.fecha(), orden.estado()});
                areaDetalle.setText(orden.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la orden.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }

    private void crearOrden(ActionEvent e) {
        try {
            String descripcionTipo = ((TipoMovimiento) cmbTipo.getSelectedItem()).getDescripcion();
            TipoMovimiento tipo = TipoMovimiento.fromDescripcion(descripcionTipo);
            OrdenMovimientoDto nueva = new OrdenMovimientoDto(
                    0,
                    tipo,
                    LocalDateTime.now(),
                    EstadosOrdenes.PENDIENTE,
                    List.of()
            );
            // Aquí podrías abrir un diálogo para agregar detalles
            servicio.crearOrden(nueva);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Orden creada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear: " + ex.getMessage());
        }
    }

    private void modificarEstado(ActionEvent e) {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para modificar su estado.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        EstadosOrdenes nuevoEstado = EstadosOrdenes.fromDescripcion(((EstadosOrdenes) cmbEstado.getSelectedItem()).getDescripcion());
        try {
            servicio.modificarEstado(id, nuevoEstado);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Estado modificado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar estado: " + ex.getMessage());
        }
    }

    private void actualizarOrden(ActionEvent e) {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para actualizar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            OrdenMovimientoDto orden = servicio.buscarPorId(id);
            // Aquí podrías abrir un diálogo para editar detalles
            servicio.actualizarOrden(orden);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Orden actualizada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminarOrden(ActionEvent e) {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            servicio.eliminarOrden(id);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Orden eliminada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}
