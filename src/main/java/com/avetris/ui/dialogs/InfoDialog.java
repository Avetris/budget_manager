package com.avetris.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextPane;

public class InfoDialog extends JDialog {

    public InfoDialog(JFrame parent, String title, String message, boolean modal) {
        super(parent, title, modal);

        setSize(300, 150);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new GridBagLayout());

        setupMessage(message);
        setupButtons();

        setVisible(true);
    }
    private void setupMessage(String message) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.weighty = 0.8f;
        c.insets = getCommonInsets();
        
        JTextPane label = new JTextPane();
        label.setContentType("text/html");
        label.setText("<html><center>" + message+ "</center></html>");
                
        label.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        label.setMargin(new Insets(0,0,0,0));
        label.setEditable(false);
        label.setFocusable(false);
        label.setOpaque(false);
        add(label, c);
    }

    private void setupButtons() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.2f;
        c.weightx = 0.5f;
        c.insets = getCommonInsets();
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> {
            dispose();
        });
        add(closeButton, c);
    }

    private Insets getCommonInsets() {
        return new Insets(3,3,3,3);
    }
}
