package com.avetris.ui.views;
import java.awt.BorderLayout;

import javax.swing.*;

import com.avetris.controllers.BillsController;
import com.avetris.controllers.ConfigController;
import com.avetris.controllers.TaskController;

public class MainWindow extends JFrame {

    static final int WIDTH = 1680;
    static final int HEIGHT = 1050;

    TasksTab taskTab;
    TaskController taskController;
    
    BillsTab billsTab;
    BillsController billController;
    
    ConfigTab configTab;
    ConfigController configController;
    
    
    public MainWindow() {
        setTitle("Facturas");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeUI();        
    }

    private void initializeUI() {
        JTabbedPane tabsPanel = new JTabbedPane();
        tabsPanel.addTab("Facturas", initBills());
        tabsPanel.addTab("Trabajos", initTasks());
        tabsPanel.addTab("Configuración", initConfig());
        add(tabsPanel, BorderLayout.CENTER);        
    }
    
    private JPanel initBills() {
        billsTab = new BillsTab();
        billController = new BillsController(billsTab);
        add(billsTab);
        return billsTab;
    }

    private JPanel initTasks() {
        taskTab = new TasksTab();
        taskController = new TaskController(taskTab);
        add(taskTab);
        return taskTab;
    }

    private JPanel initConfig() {
        configTab = new ConfigTab();
        configController = new ConfigController(configTab);
        add(configTab);
        return configTab;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}