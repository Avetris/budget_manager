package com.avetris.ui.dialogs;

import java.awt.AWTKeyStroke;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.avetris.controllers.BillsController;
import com.avetris.models.Bill;
import com.avetris.models.Client;

public class ModifyBillDialog extends JDialog implements WindowListener {

    BillsController controller;

    JTextField billIdField;
    JTextField billProjectField;
    JTextField billDateField;
    JTextField billClientNifField;
    JTextField billClientDniField;
    JTextField billClientNameField;   
    JTextField billClientAddressField;

    public ModifyBillDialog(JFrame parent, String title, BillsController controller, boolean modal) {
        super(parent, title, modal);

        this.controller = controller;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new GridBagLayout());
        setupId();
        setupDate();
        setupProject();

        setupSaveButton();

        addWindowListener(this);

        setVisible(true);
    }

    public void setupId() {
        String id = controller.getModel().getId();
        JLabel label = new JLabel();
        label.setText("Id");
        billIdField = new JTextField(id);
        label.setLabelFor(billIdField);
        billIdField.setEditable(id != null && !id.isEmpty());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.45;
        add(billIdField, c);
    }

    public void setupDate() {
        JLabel label = new JLabel();
        label.setText("Fecha");
        billDateField = new JTextField(controller.getModel().getDate());
        label.setLabelFor(billDateField);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 2;
        c.weightx = 0.45;
        add(billIdField, c); 
    }
    public void setupProject() {
        JLabel label = new JLabel();
        label.setText("Proyecto");
        billProjectField = new JTextField(controller.getModel().getProject());
        label.setLabelFor(billProjectField);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.9;
        add(billIdField, c); 
    }

    private void setupSaveButton() {
        JButton submitButton = new JButton("Guardar");
        submitButton.addActionListener(e -> {
            controller.onSubmit(
                new Bill(
                    billIdField.getText(),
                    billProjectField.getText(),
                    billDateField.getText(),
                    new Client(
                        billClientNifField.getText(), 
                        billClientDniField.getText(), 
                        billClientNameField.getText(), 
                        billClientAddressField.getText())
            ));
            dispose();
        });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(submitButton, c);
    }

    private Insets getCommonInsets() {
        return new Insets(3,3,3,3);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {
        this.controller.closeDialog();
    }

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
