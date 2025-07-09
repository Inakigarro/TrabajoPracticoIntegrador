package main.java.IG.views;

import javax.swing.*;

public class MainWindow {
    private JLabel mainTitle = new JLabel("Ventana Principal");
    private JButton ingresoButton = new JButton("Ingreso");
    private JButton egresoButton = new JButton("Egreso");
    private JPanel mainPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();


    public MainWindow() {
        mainPanel.add(mainTitle);
        buttonPanel.add(ingresoButton);
        buttonPanel.add(egresoButton);
        mainPanel.add(buttonPanel);

        ingresoButton.addActionListener(e -> {
            System.out.println("Botón de ingreso presionado");
        });
    }

    public JPanel getContentPane() {
        return mainPanel;
    }
}
