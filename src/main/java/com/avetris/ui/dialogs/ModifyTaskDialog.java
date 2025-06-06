package com.avetris.ui.dialogs;

import java.awt.AWTKeyStroke;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.swing.text.NumberFormatter;

import com.avetris.controllers.TaskController;
import com.avetris.models.Task;

public class ModifyTaskDialog extends JDialog implements WindowListener {

    TaskController controller;

    JTextField taskTitle;
    JTextArea taskDescription;
    JFormattedTextField taskPrice;

    public ModifyTaskDialog(JFrame parent, String title, TaskController controller, boolean modal) {
        super(parent, title, modal);

        this.controller = controller;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new GridBagLayout());
        setupTitle();
        setupDescription();
        setupPrice();

        setupSaveButton();

        addWindowListener(this);

        setVisible(true);
    }

    public void setupTitle() {
        JLabel label = new JLabel();
        label.setText("Titulo");
        taskTitle = new JTextField(controller.getModel().getTitle());
        label.setLabelFor(taskTitle);
        taskTitle.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                taskTitle.setText(taskTitle.getText().toUpperCase());
            }
            
        });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.9;
        add(taskTitle, c);        
    }

    public void setupDescription() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.85f;
        c.insets = getCommonInsets();

        
        JLabel label = new JLabel();
        label.setText("Descipción");
        taskDescription = new JTextArea(controller.getModel().getDescription());
        taskDescription.setLineWrap(true);
        taskDescription.setWrapStyleWord(true);
        label.setLabelFor(taskDescription);
        Set<AWTKeyStroke> forward = new HashSet<AWTKeyStroke>(label.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forward.add(KeyStroke.getKeyStroke("TAB"));
        taskDescription.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forward);        
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.8;
        add(taskDescription, c);   
    }


    public void setupPrice() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();

        
        JLabel label = new JLabel();
        label.setText("Precio");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(10000000.0);
        formatter.setAllowsInvalid(false);
        taskPrice = new JFormattedTextField(formatter);
        taskPrice.setValue(controller.getModel().getPrice());
        label.setLabelFor(taskPrice);
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.8;
        add(taskPrice, c);
    }

    private void setupSaveButton() {
        JButton submitButton = new JButton("Guardar");
        submitButton.addActionListener(e -> {
            controller.onSubmit(new Task(
                taskTitle.getText().toUpperCase(),
                taskDescription.getText(),
                (double) taskPrice.getValue()
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
