package com.avetris;

import javax.swing.SwingUtilities;

import com.avetris.ui.views.MainWindow;

public class App {
    public static void main(String[] args) {
        // Initialize the application
        SwingUtilities.invokeLater(() -> {
            // Create and display the main window
            new MainWindow().setVisible(true);
        });
    }
}