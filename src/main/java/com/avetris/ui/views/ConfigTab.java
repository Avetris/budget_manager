package com.avetris.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.avetris.listeners.IConfigListener;
import com.avetris.models.Config;

public class ConfigTab extends JPanel   {

    IConfigListener listener;    

    private JTextField nameField;
    private JTextField logoField;
    private JTextField addressField;
    private JTextField webField;
    private JFormattedTextField phoneField;
    private JTextField nifField;
    private JTextField emailField;
    private JTextField[] infoField;
    private JTextArea conditionsField;
    private JTextArea garantyField;

    public ConfigTab() {
        setLayout(new GridBagLayout());
        createView();
        addComponentListener ( new ComponentAdapter ()
        {
            public void componentShown ( ComponentEvent e )
            {
                updateView();
            }    
            public void componentHidden ( ComponentEvent e ){}
        } );
    }

    public void addListener(IConfigListener listener) {
        this.listener = listener;
    }

    public void createView() {
        createName(0, 0.1f);
        createWeb(1, 0.1f);
        createAddress(2, 0.1f);
        createPhone(3, 0.1f);
        createNif(4, 0.1f);
        createEmail(5, 0.1f);
        createConditions(6, 0.2f);
        createGaranty(7, 0.2f);
    }

    private GridBagConstraints getGridBagConstraints(int posX, int posY, int height, float weight) {
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = posX;
        c.gridy = posY;
        c.weightx = posX > 0 ? 0.9 : 0.1;
        c.weighty = weight;
        c.insets = new Insets(3,3,3,3);
        return c;
    }

    private void createName(int posY, float weight) {
        JLabel label = new JLabel("Nombre");
        nameField = new JTextField();
        label.setLabelFor(nameField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(nameField, getGridBagConstraints(1, posY, 1, weight));
    }

    private void createWeb(int posY, float weight) {
        JLabel label = new JLabel("Web");
        webField = new JTextField();
        label.setLabelFor(webField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(webField, getGridBagConstraints(1, posY, 1, weight));
    }

    private void createAddress(int posY, float weight) {
        JLabel label = new JLabel("Dirección");
        addressField = new JTextField();
        label.setLabelFor(addressField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(addressField, getGridBagConstraints(1, posY, 1, weight));
    }

    private void createPhone(int posY, float weight) {
        try{
            JLabel label = new JLabel("Teléfono");
            phoneField = new JFormattedTextField(new MaskFormatter("#########"));
            label.setLabelFor(phoneField);
    
            add(label, getGridBagConstraints(0, posY, 1, weight));
            add(phoneField, getGridBagConstraints(1, posY, 1, weight));

        } catch (Exception e) {}
    }
    private void createNif(int posY, float weight) {
        JLabel label = new JLabel("NIF");
        nifField = new JTextField();
        label.setLabelFor(nifField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(nifField, getGridBagConstraints(1, posY, 1, weight));
    }

    private void createEmail(int posY, float weight) {
        JLabel label = new JLabel("Email");
        emailField = new JTextField();
        label.setLabelFor(emailField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(emailField, getGridBagConstraints(1, posY, 1, weight));
    }

    private void createConditions(int posY, float weight) {
        JLabel label = new JLabel("Condiciones de Uso");
        conditionsField = new JTextArea();
        label.setLabelFor(conditionsField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(conditionsField, getGridBagConstraints(1, posY, 1, weight));
    }

    private void createGaranty(int posY, float weight) {
        JLabel label = new JLabel("Garantía");
        garantyField = new JTextArea();
        label.setLabelFor(garantyField);

        add(label, getGridBagConstraints(0, posY, 1, weight));
        add(garantyField, getGridBagConstraints(1, posY, 1, weight));
    }

    public void updateView() {
        if(listener != null) {
            Config config = listener.getConfig();
            

        }     
    }
}