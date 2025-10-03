package IG.views.ubicaciones;

import IG.application.ServicioUbicaciones;
import IG.application.interfaces.IServicioUbicaciones;
import IG.views.ubicaciones.forms.UbicacionesForm;
import IG.views.ubicaciones.forms.ZonaForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class UbicacionesView {
    private static final String CARD_ZONA = "ZONA";
    private static final String CARD_UBICACION = "UBICACION";

    private static final IServicioUbicaciones servicio = new ServicioUbicaciones();

    public static JPanel buildUI() {
        var root = new JPanel(new BorderLayout(12,12));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Top: selector de entidad
        var cbEntidad = new JComboBox<>(new String[]{"Zona", "Ubicacion"});
        var naveButton = new JButton("Crear Nave");
        naveButton.addActionListener(e -> {
            try {
                servicio.crearNave();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(root, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        var top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Tipo de entidad:"));
        top.add(cbEntidad);
        top.add(naveButton);
        root.add(top, BorderLayout.NORTH);

        // Center: cards
        var cards = new JPanel(new CardLayout());
        var formZona = new ZonaForm(servicio);
        var formUbic = new UbicacionesForm(servicio);

        cards.add(formZona, CARD_ZONA);
        cards.add(formUbic, CARD_UBICACION);
        root.add(cards, BorderLayout.CENTER);

        // Bottom: acciones
        var btnGuardar = new JButton("Guardar");
        var btnCerrar = new JButton("Cerrar");
        var bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnCerrar);
        bottom.add(btnGuardar);
        root.add(bottom, BorderLayout.SOUTH);

        // Cambio de card
        cbEntidad.addActionListener((ActionEvent e) -> {
            var cl = (CardLayout) cards.getLayout();
            switch (Objects.toString(cbEntidad.getSelectedItem(), "")) {
                case "Zona" -> cl.show(cards, CARD_ZONA);
                default -> cl.show(cards, CARD_UBICACION);
            }
        });

        // Guardar
        btnGuardar.addActionListener(e -> {
            var sel = Objects.toString(cbEntidad.getSelectedItem(), "");
            try {
                switch (sel) {
                    case "Zona" -> {
                        var z = formZona.buildEntity();
                        servicio.crearZona(z);
                        formZona.afterSave(z);
                    }
                    case "Ubicacion" -> {
                        var u = formUbic.buildEntity();
                        servicio.crearUbicacion(u);
                        formUbic.afterSave(u);
                    }
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(root, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(root, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCerrar.addActionListener(e -> SwingUtilities.getWindowAncestor(root).dispose());
        return root;
    }
}