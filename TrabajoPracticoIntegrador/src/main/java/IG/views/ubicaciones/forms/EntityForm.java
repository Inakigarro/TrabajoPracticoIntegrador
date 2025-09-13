package IG.views.ubicaciones.forms;

import javax.swing.*;
import java.awt.*;

public abstract class EntityForm<T> extends JPanel {
    protected final GridBagConstraints gbc;
    EntityForm() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = -1; gbc.anchor = GridBagConstraints.WEST;
    }
    protected void addRow(String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(4,0,4,8);
        add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4,0,4,0);
        add(field, gbc);
    }
    abstract T buildEntity() throws IllegalArgumentException;
    abstract void afterSave(T entity);
}
