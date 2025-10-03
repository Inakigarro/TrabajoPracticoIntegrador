package IG.views.ubicaciones.forms;

import IG.application.Dtos.Ubicacion.NaveDto;
import IG.application.Dtos.Ubicacion.ZonaDto;
import IG.application.interfaces.IServicioUbicaciones;
import IG.domain.Clases.Nave;
import IG.domain.Clases.Ubicacion;
import IG.domain.Clases.Zona;
import IG.domain.Enums.TipoZona;

import javax.swing.*;

import java.util.ArrayList;

public class UbicacionesForm extends EntityForm<Ubicacion> {
    private final JSpinner spEst = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private final JSpinner spNivel = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JFormattedTextField txtCap = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());
    private final JComboBox<NaveDto> cbNave = new JComboBox<>();
    private final JComboBox<ZonaDto> cbZona = new JComboBox<>();
    private IServicioUbicaciones servicio;

    public UbicacionesForm(IServicioUbicaciones servicio) {
        this.servicio = servicio;
        txtCap.setValue(1250.0);

        refreshNaves();
        refreshZonas();

        addRow("Nave:", cbNave);
        addRow("Zona:", cbZona);
        addRow("Nro. Estantería:", spEst);
        addRow("Nro. Nivel:", spNivel);
        addRow("Capacidad (kg):", txtCap);

        // Dependencias: al cambiar Nave, se actualiza el combo de Zonas
        cbNave.addActionListener(e -> refreshZonas());

        var btnRefresh = new JButton("Actualizar Naves/Zonas");
        btnRefresh.addActionListener(e -> { refreshNaves(); refreshZonas(); });
        addRow("", btnRefresh);
    }

    private void refreshNaves() {
        var sel = cbNave.getSelectedItem();
        cbNave.removeAllItems();
        var naves = this.servicio.obtenerNaves();
        for (var n : naves) cbNave.addItem(n);
        if (sel != null) cbNave.setSelectedItem(sel);
    }

    private void refreshZonas() {
        var nave = (NaveDto) cbNave.getSelectedItem();
        var zonas = nave == null ? new ArrayList<ZonaDto>() : this.servicio.obtenerZonas(nave.id());
        var sel = cbZona.getSelectedItem();
        cbZona.removeAllItems();
        for (var z : zonas) cbZona.addItem(z);
        if (sel != null) cbZona.setSelectedItem(sel);
    }

    @Override public Ubicacion buildEntity() throws IllegalArgumentException {
        var selectedZona = (ZonaDto) cbZona.getSelectedItem();
        if (selectedZona == null) throw new IllegalArgumentException("Debe seleccionar una Zona.");
        ZonaDto zonaDto = this.servicio.obtenerZonaPorId(selectedZona.id());
        if (zonaDto == null) throw new IllegalArgumentException("Zona inválida.");
        Nave nave = new Nave();
        nave.setId(zonaDto.nave().id());
        Zona zona = new Zona(TipoZona.valueOf(zonaDto.tipo().toUpperCase()));
        zona.setId(zonaDto.id());
        zona.setNave(nave);
        int est = (int) spEst.getValue();
        int nivel = (int) spNivel.getValue();
        double cap;
        try {
            txtCap.commitEdit();
            cap = ((Number) txtCap.getValue()).doubleValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Capacidad inválida.");
        }
        if (cap <= 0) throw new IllegalArgumentException("La capacidad debe ser positiva.");
        return new Ubicacion(est, nivel, zona);
    }

    @Override public void afterSave(Ubicacion u) {
        JOptionPane.showMessageDialog(this, "Ubicación creada (#" + u.getId() + ") en Zona #" + u.getZona().getId());
    }
}
