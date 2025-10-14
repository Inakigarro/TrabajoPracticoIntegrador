package IG.views;

import IG.application.Dtos.OrdenTransformacion.OrdenTransformacionDto;
import IG.application.ServicioOrdenTransformacion;
import IG.application.interfaces.IServicioOrdenTransformacion;
import IG.domain.Enums.EstadosOrdenes;
import IG.domain.Enums.TipoTransformacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenTransformacionViewModerna extends JFrame {
    private final IServicioOrdenTransformacion servicio;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscarId;
    private JComboBox<TipoTransformacion> cmbTipo;
    private JButton btnCrear, btnBuscar, btnModificarEstado, btnActualizar, btnCancelar, btnRefrescar;

    public OrdenTransformacionViewModerna() {
        this.servicio = new ServicioOrdenTransformacion();
        setTitle("Gestión Moderna de Órdenes de Transformación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        JPanel panelAcciones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtBuscarId = new JTextField(8);
        cmbTipo = new JComboBox<>(TipoTransformacion.values());
        btnBuscar = new JButton("Buscar por ID");
        btnCrear = new JButton("Crear Orden");
        btnModificarEstado = new JButton("Modificar Estado");
        btnActualizar = new JButton("Actualizar");
        btnCancelar = new JButton("Cancelar");
        btnRefrescar = new JButton("Refrescar");

        gbc.gridx = 0; gbc.gridy = 0; panelAcciones.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; panelAcciones.add(txtBuscarId, gbc);
        gbc.gridx = 2; panelAcciones.add(btnBuscar, gbc);
        gbc.gridx = 3; panelAcciones.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 4; panelAcciones.add(cmbTipo, gbc);
        gbc.gridx = 7; panelAcciones.add(btnCrear, gbc);
        gbc.gridx = 8; panelAcciones.add(btnModificarEstado, gbc);
        gbc.gridx = 9; panelAcciones.add(btnActualizar, gbc);
        gbc.gridx = 10; panelAcciones.add(btnRefrescar, gbc);
        gbc.gridx = 11; panelAcciones.add(btnCancelar, gbc);

        add(panelAcciones, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Tipo", "Fecha", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaOrdenes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaOrdenes);
        add(scrollTabla, BorderLayout.CENTER);
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnBuscar.addActionListener(this::buscarOrden);
        btnCrear.addActionListener(this::crearOrden);
        btnModificarEstado.addActionListener(this::modificarEstado);
        btnActualizar.addActionListener(this::actualizarOrden);
        btnCancelar.addActionListener(this::cancelarOrden);

        tablaOrdenes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && tablaOrdenes.getSelectedRow() != -1) {
                    int fila = tablaOrdenes.getSelectedRow();
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    // Aquí puedes crear una vista de detalles si la necesitas
                    // OrdenTransformacionDetalleView detalleView = new OrdenTransformacionDetalleView(servicio, id);
                    // detalleView.setVisible(true);
                    JOptionPane.showMessageDialog(OrdenTransformacionViewModerna.this,
                            "Detalles de orden ID: " + id + "\n(Pendiente de implementar vista de detalles)");
                }
            }
        });

        btnCancelar.setEnabled(false);
        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaOrdenes.getSelectedRow();
            if (fila == -1) {
                btnCancelar.setEnabled(false);
                btnModificarEstado.setText("Modificar Estado");
                return;
            }

            Object estadoObj = modeloTabla.getValueAt(fila, 3);
            boolean isCancelado = false;
            boolean isCompletado = false;

            if (estadoObj instanceof EstadosOrdenes estado) {
                isCancelado = estado == EstadosOrdenes.CANCELADO;
                isCompletado = estado == EstadosOrdenes.COMPLETADO;
                btnCancelar.setEnabled(estado == EstadosOrdenes.PENDIENTE || estado == EstadosOrdenes.PROCESO);

                if (estado == EstadosOrdenes.PENDIENTE) {
                    btnModificarEstado.setText("Aprobar");
                } else if (estado == EstadosOrdenes.APROBADO) {
                    btnModificarEstado.setText("Ejecutar");
                } else if (estado == EstadosOrdenes.PROCESO) {
                    btnModificarEstado.setText("Finalizar");
                } else {
                    btnModificarEstado.setText("Modificar Estado");
                }
            } else if (estadoObj instanceof String estadoStr) {
                isCancelado = "CANCELADO".equalsIgnoreCase(estadoStr);
                isCompletado = "COMPLETADO".equalsIgnoreCase(estadoStr);
                btnCancelar.setEnabled("PENDIENTE".equalsIgnoreCase(estadoStr) || "PROCESO".equalsIgnoreCase(estadoStr));

                if ("PENDIENTE".equalsIgnoreCase(estadoStr)) {
                    btnModificarEstado.setText("Aprobar");
                } else if ("APROBADA".equalsIgnoreCase(estadoStr)) {
                    btnModificarEstado.setText("Ejecutar");
                } else if ("PROCESO".equalsIgnoreCase(estadoStr)) {
                    btnModificarEstado.setText("Finalizar");
                } else {
                    btnModificarEstado.setText("Modificar Estado");
                }
            } else {
                btnCancelar.setEnabled(false);
                btnModificarEstado.setText("Modificar Estado");
            }

            if (isCancelado || isCompletado) {
                tablaOrdenes.clearSelection();
                btnCancelar.setEnabled(false);
                btnModificarEstado.setText("Modificar Estado");
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<OrdenTransformacionDto> ordenes = servicio.listarOrdenes(100, 1);
            for (OrdenTransformacionDto o : ordenes) {
                modeloTabla.addRow(new Object[]{o.id(), o.tipo(), o.fecha(), o.estado()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar órdenes: " + ex.getMessage());
        }
    }

    private void buscarOrden(ActionEvent e) {
        try {
            int id = Integer.parseInt(txtBuscarId.getText().trim());
            OrdenTransformacionDto orden = servicio.buscarPorId(id);
            if (orden != null) {
                modeloTabla.setRowCount(0);
                modeloTabla.addRow(new Object[]{orden.id(), orden.tipo(), orden.fecha(), orden.estado()});
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la orden.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }

    private void crearOrden(ActionEvent e) {
        try {
            TipoTransformacion tipo = (TipoTransformacion) cmbTipo.getSelectedItem();
            OrdenTransformacionDto nueva = new OrdenTransformacionDto(
                    0,
                    tipo,
                    LocalDateTime.now(),
                    EstadosOrdenes.PENDIENTE,
                    List.of()
            );

            servicio.crearOrden(nueva);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Orden de transformación creada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear orden: " + ex.getMessage());
        }
    }

    private void modificarEstado(ActionEvent e) {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para modificar su estado.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            servicio.modificarEstado(id);
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
            OrdenTransformacionDto orden = servicio.buscarPorId(id);
            if (orden != null) {
                JOptionPane.showMessageDialog(this,
                        "Funcionalidad de actualización pendiente de implementar.\n" +
                                "Orden ID: " + orden.id() + "\n" +
                                "Tipo: " + orden.tipo() + "\n" +
                                "Estado: " + orden.estado());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void cancelarOrden(ActionEvent e) {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para cancelar.");
            return;
        }

        Object estadoObj = modeloTabla.getValueAt(fila, 3);
        if (estadoObj instanceof EstadosOrdenes estado) {
            if (estado == EstadosOrdenes.CANCELADO) {
                JOptionPane.showMessageDialog(this, "No se puede cancelar una orden ya cancelada.");
                return;
            }
            if (estado == EstadosOrdenes.COMPLETADO) {
                JOptionPane.showMessageDialog(this, "No se puede cancelar una orden completada.");
                return;
            }
        } else if (estadoObj instanceof String estadoStr) {
            if ("CANCELADO".equalsIgnoreCase(estadoStr)) {
                JOptionPane.showMessageDialog(this, "No se puede cancelar una orden ya cancelada.");
                return;
            }
            if ("COMPLETADO".equalsIgnoreCase(estadoStr)) {
                JOptionPane.showMessageDialog(this, "No se puede cancelar una orden completada.");
                return;
            }
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            servicio.cancelarOrden(id);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Orden cancelada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cancelar: " + ex.getMessage());
        }
    }
}