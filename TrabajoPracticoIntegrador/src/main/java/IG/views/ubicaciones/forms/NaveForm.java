package IG.views.ubicaciones.forms;

import IG.application.Dtos.NaveDto;
import IG.application.interfaces.IServicioUbicaciones;
import IG.domain.Clases.Nave;

import javax.swing.*;

public class NaveForm extends EntityForm<NaveDto> {
    private final JLabel lblInfo = new JLabel("El id se autogenera al guardar");
    private final IServicioUbicaciones servicio;

    public NaveForm(IServicioUbicaciones servicio) {
        this.servicio = servicio;
        addRow("Id:", new JLabel("(autogenerado)"));
    }

    @Override
    NaveDto buildEntity() throws IllegalArgumentException {
        return null;
    }

    @Override public void afterSave(NaveDto n) {
        JOptionPane.showMessageDialog(this, "Nave creada con id=" + n.id());
    }
}
