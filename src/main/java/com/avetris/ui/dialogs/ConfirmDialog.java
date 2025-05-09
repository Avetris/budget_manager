package com.avetris.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import com.avetris.listeners.IConfirmListener;

public class ConfirmDialog extends JDialog {

    public ConfirmDialog(JFrame parent, String title, String message, boolean modal, IConfirmListener listener) {
        super(parent, title, modal);

        setSize(300, 125);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new GridBagLayout());

        setupMessage(message);        
        setupButtons(listener);

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

    private void setupButtons(IConfirmListener listener) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.2f;
        c.weightx = 0.5f;
        c.insets = getCommonInsets();
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
            dispose();
        });
        add(cancelButton, c);
        JButton submitButton = new JButton("Aceptar");
        submitButton.addActionListener(e -> {
            listener.onConfirm();
            dispose();
        });
        c.gridx = 1;
        add(submitButton, c);
    }

    private Insets getCommonInsets() {
        return new Insets(3,3,3,3);
    }
}
