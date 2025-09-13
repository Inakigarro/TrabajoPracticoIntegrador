package IG.views.ubicaciones.forms;

import IG.application.Dtos.NaveDto;
import IG.application.interfaces.IServicioUbicaciones;
import IG.domain.Clases.Nave;
import IG.domain.Clases.Zona;
import IG.domain.Enums.TipoZona;

import javax.swing.*;
import java.util.Objects;

public class ZonaForm extends EntityForm<Zona> {
    private final JComboBox<TipoZona> cbTipo = new JComboBox<>(TipoZona.values());
    private final JComboBox<NaveDto> cbNave = new JComboBox<>();

    private IServicioUbicaciones servicio;

    public ZonaForm(IServicioUbicaciones servicio) {
        this.servicio = servicio;
        refreshNaves();
        addRow("Tipo de zona:", cbTipo);
        addRow("Nave:", cbNave);

        JButton btnRefresh = new JButton("Actualizar Naves");
        btnRefresh.addActionListener(e -> refreshNaves());
        addRow("", btnRefresh);
    }

    private void refreshNaves() {
        var sel = cbNave.getSelectedItem();
        cbNave.removeAllItems();
        var naves = servicio.obtenerNaves();
        for (var n : naves) cbNave.addItem(n);
        if (sel != null) cbNave.setSelectedItem(sel);
    }

    @Override public Zona buildEntity() throws IllegalArgumentException {
        var tipo = (TipoZona) Objects.requireNonNull(cbTipo.getSelectedItem(), "Tipo obligatorio");
        var naveDto = (NaveDto) cbNave.getSelectedItem();
        if (naveDto == null) throw new IllegalArgumentException("Debe seleccionar una Nave.");
        Nave n = new Nave();
        n.setId(naveDto.id());
        Zona zona = new Zona(tipo);
        zona.setNave(n);
        return zona;
    }

    @Override public void afterSave(Zona z) {
        JOptionPane.showMessageDialog(this, "Zona creada (#" + z.getId() + ") en " + z.getNave());
    }
}
