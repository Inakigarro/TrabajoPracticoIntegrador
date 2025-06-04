package IG.views;

import javax.swing.*;

public class MainWindow {
    private JLabel mainTitle;
    private JButton ingresoButton;
    private JButton egresoButton;


    public MainWindow() {
        ingresoButton.addActionListener(e -> {
            System.out.println("Botón de ingreso presionado");
        });
    }
}
