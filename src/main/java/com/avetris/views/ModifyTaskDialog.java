package com.avetris.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import com.avetris.interfaces.ITaskEdit;
import com.avetris.utils.PropertyNames;

public class ModifyTaskDialog extends JDialog implements IViewPanel {

    ITaskEdit controller;

    JTextField taskTitle;
    JTextArea taskDescription;
    JFormattedTextField taskPrice;

    public ModifyTaskDialog(JFrame parent, String title, boolean show) {
        super(parent, title, show);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new GridBagLayout());
        setupTitle();
        setupDescription();
        setupPrice();

        setupSaveButton();
    }

    public void attach(ITaskEdit controller) {
        this.controller = controller;
    }

    public void setupTitle() {
        JLabel label = new JLabel();
        label.setText("Titulo");
        taskTitle = new JTextField("Esto es una prueba");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.8;
        add(taskTitle, c);        
    }

    public void setupDescription() {
        JLabel label = new JLabel();
        label.setText("Descipción");
        taskDescription = new JTextArea("Esto es una prueba");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 40;
        c.weightx = 0.2;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.8;
        add(taskDescription, c);   
    }


    public void setupPrice() {
        JLabel label = new JLabel();
        label.setText("Precio");
        taskPrice = new JFormattedTextField(new NumberFormatter());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.2;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.8;
        add(taskPrice, c);
    }

    private void setupSaveButton() {
        JButton submitButton = new JButton("Guardar");
        submitButton.addActionListener(e -> {
            controller.onSubmit();
            dispose();
        });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = getCommonInsets();
        add(submitButton, c);
    }

    private Insets getCommonInsets() {
        return new Insets(3,3,3,3);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case PropertyNames.Task.TITLE:
                taskTitle.setText(evt.getNewValue().toString());            
                break;
            case PropertyNames.Task.DESCRIPTION:
                taskDescription.setText(evt.getNewValue().toString());
                break;
            case PropertyNames.Task.PRICE:
                taskPrice.setText(evt.getNewValue().toString());                
                break;
            default:
                break;
        }
    }    
}
