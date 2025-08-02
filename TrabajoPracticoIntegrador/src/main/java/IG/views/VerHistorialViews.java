package main.java.IG.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VerHistorialViews extends JFrame {

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public VerHistorialViews() {
        setTitle("Historial de Movimientos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel();
        tablaHistorial = new JTable(modeloTabla);

        modeloTabla.addColumn("ID Orden");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Ubicación");

        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCerrar);
        add(bottomPanel, BorderLayout.SOUTH);

        cargarDatosDummy(); // temporal, después se conecta a BD
    }

    private void cargarDatosDummy() {
        List<String[]> datos = new ArrayList<>();
        datos.add(new String[]{"1", "Ingreso", "2024-06-01", "Completado", "Producto A", "50", "Est. 3"});
        datos.add(new String[]{"2", "Egreso", "2024-06-05", "Pendiente", "Producto B", "30", "Est. 2"});
        datos.add(new String[]{"3", "Interno", "2024-06-07", "Aprobado", "Producto C", "20", "Est. 1 → Est. 4"});

        for (String[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
}
