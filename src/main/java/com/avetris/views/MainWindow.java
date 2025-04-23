package com.avetris.views;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    

    public MainWindow() {
        setTitle("Facturas");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeUI();
    }

    private void InitializeManagers() {

    }

    private void initializeUI() {
        /*JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Welcome to the Java Desktop Application", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JButton button = new JButton("Click Me");
        button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Button Clicked!"));
        panel.add(button, BorderLayout.SOUTH);*/

        ModifyTaskDialog diag = new ModifyTaskDialog(this, "Modificar Trabajo", true);
        diag.setVisible(true);
        //add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}