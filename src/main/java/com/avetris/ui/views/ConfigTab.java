package com.avetris.ui.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.avetris.listeners.IConfigListener;
import com.avetris.models.Config;

public class ConfigTab extends JPanel   {

    IConfigListener listener;    

    private JTextField nameField;
    private JLabel logoLabel;
    private JTextField addressField;
    private JTextField webField;
    private JFormattedTextField phoneField;
    private JTextField nifField;
    private JTextField emailField;
    private JTextArea infoField;
    private JTextArea conditionsField;
    private JTextArea garantyField;

    public ConfigTab() {
        setLayout(new BorderLayout());
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        createLogo(panel, 0, 0.1f);
        createName(panel, 1, 0.05f);
        createWeb(panel, 2, 0.05f);
        createAddress(panel, 3, 0.05f);
        createPhone(panel, 4, 0.05f);
        createNif(panel, 5, 0.05f);
        createEmail(panel, 6, 0.05f);
        createInfo(panel, 7, 0.2f);
        createConditions(panel, 8, 0.2f);
        createGaranty(panel, 9, 0.2f);

        JPanel bottom = new JPanel();
        createButton(bottom);

        add(panel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private GridBagConstraints getGridBagConstraints(int posX, int posY, boolean equalX, float weight) {
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = posX;
        c.gridy = posY;
        if(!equalX) {
            c.weightx = posX > 0 ? 0.9 : 0.1;
        }
        c.weighty = weight;
        c.insets = new Insets(3,3,3,3);
        return c;
    }

    private void createLogo(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Icono");
        logoLabel = new JLabel();

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(logoLabel, getGridBagConstraints(1, posY, false, weight));
    }

    private void createName(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Nombre");
        nameField = new JTextField();
        label.setLabelFor(nameField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(nameField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createWeb(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Web");
        webField = new JTextField();
        label.setLabelFor(webField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(webField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createAddress(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Dirección");
        addressField = new JTextField();
        label.setLabelFor(addressField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(addressField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createPhone(JPanel panel, int posY, float weight) {
        try{
            JLabel label = new JLabel("Teléfono");
            phoneField = new JFormattedTextField(new MaskFormatter("#########"));
            label.setLabelFor(phoneField);
    
            panel.add(label, getGridBagConstraints(0, posY, false, weight));
            panel.add(phoneField, getGridBagConstraints(1, posY, false, weight));

        } catch (Exception e) {}
    }
    private void createNif(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("NIF");
        nifField = new JTextField();
        label.setLabelFor(nifField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(nifField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createEmail(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Email");
        emailField = new JTextField();
        label.setLabelFor(emailField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(emailField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createInfo(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Info (Separado por salto de línea)");
        infoField = new JTextArea();
        infoField.setLineWrap(true);
        infoField.setWrapStyleWord(true);
        JScrollPane scrollpane = new JScrollPane(infoField);
        label.setLabelFor(infoField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(scrollpane, getGridBagConstraints(1, posY, false, weight));
    }

    private void createConditions(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Condiciones de Uso");
        conditionsField = new JTextArea();
        conditionsField.setLineWrap(true);
        conditionsField.setWrapStyleWord(true);
        JScrollPane scrollpane = new JScrollPane(conditionsField);
        label.setLabelFor(conditionsField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(scrollpane, getGridBagConstraints(1, posY, false, weight));
    }

    private void createGaranty(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Garantía");
        garantyField = new JTextArea();
        garantyField.setLineWrap(true);
        garantyField.setWrapStyleWord(true);
        JScrollPane scrollpane = new JScrollPane(garantyField);
        label.setLabelFor(garantyField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(scrollpane, getGridBagConstraints(1, posY, false, weight));
    }

    private void createButton(JPanel panel) {
        JButton buttonRevert = new JButton("Revertir");
        buttonRevert.addActionListener(l -> updateView());
        JButton buttonSave = new JButton("Guardar");
        buttonSave.addActionListener(l -> {
            if(listener != null) {
                listener.onSubmit(new Config(
                    nameField.getText(),
                    addressField.getText(),
                    webField.getText(),
                    phoneField.getText(),
                    nifField.getText(),
                    emailField.getText(),
                    infoField.getText().split("\n"),
                    conditionsField.getText(),
                    garantyField.getText()
                ));
            }
        });

        panel.add(buttonRevert, getGridBagConstraints(0, 0, true, 1f));
        panel.add(buttonSave, getGridBagConstraints(1, 0, true, 1f));
    }


    public void updateView() {
        if(listener != null) {
            Config config = listener.getConfig();
            setIcon();
            nameField.setText(config.getName());
            webField.setText(config.getWeb());
            addressField.setText(config.getAddress());
            phoneField.setText(config.getPhone());
            nifField.setText(config.getNif());
            emailField.setText(config.getEmail());
            infoField.setText(String.join("\n", config.getInfo()));
            conditionsField.setText(config.getConditions());
            garantyField.setText(config.getGaranty());
        }     
    }

    private void setIcon() {
        BufferedImage myPicture;
        String path = listener.GetIcon();
        logoLabel.setText(null);
        logoLabel.setIcon(null);
        int size = Math.min(logoLabel.getWidth(), logoLabel.getHeight());
        try {
            myPicture = ImageIO.read(new File(path));
            logoLabel.setIcon(new ImageIcon(myPicture.getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            logoLabel.setText("No existe el archivo " + path); 
        }
    }
}