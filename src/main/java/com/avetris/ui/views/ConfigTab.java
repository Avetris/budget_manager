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
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import com.avetris.listeners.IConfigListener;
import com.avetris.models.Config;

public class ConfigTab extends JPanel implements DocumentListener  {

    IConfigListener listener;    

    private JTextField nameField;
    private JLabel logoLabel;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField webField;
    private JFormattedTextField phoneField;
    private JTextField nifField;
    private JTextField emailField;
    private JTextArea infoField;
    private JTextArea conditionsField;
    private JTextArea garantyField;

    private JButton buttonRevert;
    private JButton buttonSave;

    private int imageSize = 0;

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
        createStreet(panel, 3, 0.05f);
        createCity(panel, 4, 0.05f);
        createPhone(panel, 5, 0.05f);
        createNif(panel, 6, 0.05f);
        createEmail(panel, 7, 0.05f);
        createInfo(panel, 8, 0.2f);
        createConditions(panel, 9, 0.2f);
        createGaranty(panel, 10, 0.2f);

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
        nameField.getDocument().addDocumentListener(this);
        label.setLabelFor(nameField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(nameField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createWeb(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Web");
        webField = new JTextField();
        webField.getDocument().addDocumentListener(this);
        label.setLabelFor(webField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(webField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createStreet(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Dirección");
        streetField = new JTextField();
        streetField.getDocument().addDocumentListener(this);
        label.setLabelFor(streetField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(streetField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createCity(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Codigo postal Y Ciudad");
        cityField = new JTextField();
        cityField.getDocument().addDocumentListener(this);
        label.setLabelFor(cityField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(cityField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createPhone(JPanel panel, int posY, float weight) {
        try{
            JLabel label = new JLabel("Teléfono");
            phoneField = new JFormattedTextField( new MaskFormatter( "### ## ## ##" ));
            phoneField.getDocument().addDocumentListener(this);
            label.setLabelFor(phoneField);
    
            panel.add(label, getGridBagConstraints(0, posY, false, weight));
            panel.add(phoneField, getGridBagConstraints(1, posY, false, weight));

        } catch (Exception e) {}
    }
    private void createNif(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("NIF");
        nifField = new JTextField();
        nifField.getDocument().addDocumentListener(this);
        label.setLabelFor(nifField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(nifField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createEmail(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Email");
        emailField = new JTextField();
        emailField.getDocument().addDocumentListener(this);
        label.setLabelFor(emailField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(emailField, getGridBagConstraints(1, posY, false, weight));
    }

    private void createInfo(JPanel panel, int posY, float weight) {
        JLabel label = new JLabel("Info (Separado por salto de línea)");
        infoField = new JTextArea();
        infoField.getDocument().addDocumentListener(this);
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
        conditionsField.getDocument().addDocumentListener(this);
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
        garantyField.getDocument().addDocumentListener(this);
        garantyField.setLineWrap(true);
        garantyField.setWrapStyleWord(true);
        JScrollPane scrollpane = new JScrollPane(garantyField);
        label.setLabelFor(garantyField);

        panel.add(label, getGridBagConstraints(0, posY, false, weight));
        panel.add(scrollpane, getGridBagConstraints(1, posY, false, weight));
    }

    private void createButton(JPanel panel) {
        buttonRevert = new JButton("Revertir");
        buttonRevert.addActionListener(l -> updateView());
        buttonSave = new JButton("Guardar");
        buttonSave.addActionListener(l -> {
            if(listener != null) {
                listener.onSubmit(new Config(
                    nameField.getText(),
                    streetField.getText(),
                    cityField.getText(),
                    webField.getText(),
                    phoneField.getText(),
                    nifField.getText(),
                    emailField.getText(),
                    infoField.getText().split("\n"),
                    conditionsField.getText(),
                    garantyField.getText()
                ));
                validateChanges();
            }
        });
        buttonRevert.setEnabled(false);
        buttonSave.setEnabled(false);

        panel.add(buttonRevert, getGridBagConstraints(0, 0, true, 1f));
        panel.add(buttonSave, getGridBagConstraints(1, 0, true, 1f));
    }


    public void updateView() {
        if(listener != null) {
            Config config = listener.getConfig();
            setIcon();
            nameField.setText(config.getName());
            webField.setText(config.getWeb());
            streetField.setText(config.getStreet());
            cityField.setText(config.getCity());
            phoneField.setText(config.getPhone());
            nifField.setText(config.getNif());
            emailField.setText(config.getEmail());
            infoField.setText(String.join("\n", config.getInfo()));
            conditionsField.setText(config.getConditions());
            garantyField.setText(config.getGaranty());
            validateChanges();
        }     
    }

    private void setIcon() {
        BufferedImage myPicture;
        String path = listener.GetIcon();
        logoLabel.setText(null);
        logoLabel.setIcon(null);
        if(imageSize == 0) {
            imageSize = Math.min(logoLabel.getWidth(), logoLabel.getHeight());
        }
        try {
            myPicture = ImageIO.read(new File(path));            
            logoLabel.setIcon(new ImageIcon(myPicture.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            
            //logoLabel.setText("No existe el archivo " + path); 
            
            logoLabel.setText("No existe el archivo " + Paths.get("").toAbsolutePath().toString()); 
            
        }
    }

    

    @Override
    public void insertUpdate(DocumentEvent e) {
        validateChanges();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateChanges();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    private void validateChanges() {
        Config config = listener.getConfig();
        boolean modified = !nameField.getText().equals(config.getName()) || 
                            !webField.getText().equals(config.getWeb()) || 
                            !streetField.getText().equals(config.getStreet()) || 
                            !cityField.getText().equals(config.getCity()) || 
                            !nifField.getText().equals(config.getNif()) || 
                            !emailField.getText().equals(config.getEmail()) || 
                            !infoField.getText().equals(String.join("\n", config.getInfo())) || 
                            !conditionsField.getText().equals(config.getConditions()) || 
                            !garantyField.getText().equals(config.getGaranty());

        buttonSave.setEnabled(modified);
        buttonRevert.setEnabled(modified);
    }
}