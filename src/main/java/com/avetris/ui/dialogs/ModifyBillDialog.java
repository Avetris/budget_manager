package com.avetris.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.avetris.controllers.BillsController;
import com.avetris.models.Bill;
import com.avetris.models.Client;
import com.avetris.models.Task;
import com.avetris.ui.components.AutoRowHeightTable;
import com.avetris.ui.components.ButtonColumn;
import com.avetris.ui.components.JTextAreaCellRenderer;

public class ModifyBillDialog extends JDialog implements WindowListener, DocumentListener {

    BillsController controller;

    JTextField billIdField;
    JTextField billProjectField;
    JTextField billDateField;
    JTextField billClientNifField;
    JComboBox<String> billClientTypeField;
    JTextField billClientNameField;   
    JTextField billClientAddressField;
    
    JTextField billIvaField;
    JLabel billTotal;
    JLabel billTotalIva;
    JLabel billTotalWithIva;

    JComponent taskComponent;
    AutoRowHeightTable taskTable;

    JButton submitButton;
    JButton generatePdfButton;

    boolean canGenerate = false;
    boolean canSave = false;

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

        setupClientNif();
        setupClientType();
        setupClientName();
        setupClientAddress();

        setupAddTaskButton();
        setupAddTaskTable();

        setupIvaTotal();
        setupSaveButton();
        setupGeneratePdf();

        addWindowListener(this);

        setVisible(true);
    }

    public void setupId() {
        String id = controller.getModel().getId();
        JLabel label = new JLabel();
        label.setText("Id");
        billIdField = new JTextField(id);
        label.setLabelFor(billIdField);
        billIdField.setEditable(id == null || id.isEmpty());
        billIdField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.45;
        add(billIdField, c);
        canGenerate = id != null && !id.isEmpty();
    }

    public void setupDate() {
        JLabel label = new JLabel();
        label.setText("Fecha");
        billDateField = new JTextField(controller.getModel().getDate());
        label.setLabelFor(billDateField);
        billDateField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();    
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 3;
        c.weightx = 0.45;
        add(billDateField, c); 
    }

    public void setupProject() {
        JLabel label = new JLabel();
        label.setText("Proyecto");
        billProjectField = new JTextField(controller.getModel().getProject());
        label.setLabelFor(billProjectField);
        billProjectField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.05f;
        c.weightx = 0.1;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.9;
        c.gridwidth = 3;
        add(billProjectField, c); 
    }

    public void setupClientNif() {
        JLabel label = new JLabel();
        label.setText("NIF/DNI");
        billClientNifField = new JTextField(controller.getModel().getClient().getNif());
        label.setLabelFor(billClientNifField);
        billClientNifField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.5;
        c.gridwidth = 2;
        add(billClientNifField, c);
    }

    public void setupClientType() {
        billClientTypeField = new JComboBox<>();
        billClientTypeField.addItem("Empresa");
        billClientTypeField.addItem("Individual");
        billClientTypeField.addActionListener(l -> validate());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 2;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        c.weightx = 0.5;
        add(billClientTypeField, c); 
    }


    public void setupClientName() {
        JLabel label = new JLabel();
        label.setText("Nombre Cliente");
        billClientNameField = new JTextField(controller.getModel().getClient().getName());
        label.setLabelFor(billClientNameField);
        billClientNameField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.95;
        c.gridwidth = 3;
        add(billClientNameField, c);
    }

    public void setupClientAddress() {
        JLabel label = new JLabel();
        label.setText("Dirección Cliente");
        billClientAddressField = new JTextField(controller.getModel().getClient().getAddress());
        label.setLabelFor(billClientAddressField);
        billClientAddressField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.95;
        c.gridwidth = 3;
        add(billClientAddressField, c);
    }

    public void setupIvaTotal() {
      /*   JLabel label = new JLabel();
        label.setText("Dirección Cliente");
        billClientAddressField = new JTextField(controller.getModel().getClient().getAddress());
        label.setLabelFor(billClientAddressField);
        billClientAddressField.getDocument().addDocumentListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();
        add(label, c);
        c.gridx = 1;
        c.weightx = 0.95;
        c.gridwidth = 3;
        add(billClientAddressField, c);*/
    }

    private void setupSaveButton() {
        submitButton = new JButton("Guardar");
        submitButton.addActionListener(e -> {
            controller.onSubmit(
                new Bill(
                    billIdField.getText(),
                    billProjectField.getText(),
                    billDateField.getText(),
                    new Client(
                        billClientNifField.getText(), 
                        billClientTypeField.getSelectedItem().equals("Empresa"), 
                        billClientNameField.getText(), 
                        billClientAddressField.getText())
            ));
            generatePdfButton.setEnabled(canGenerate);
            submitButton.setEnabled(false);
            canGenerate = true;
            canSave = false;
        });
        submitButton.setEnabled(canSave);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(submitButton, c);
    }

    
    private void setupGeneratePdf() {
        generatePdfButton = new JButton("Generar PDF");
        generatePdfButton.addActionListener(e -> {
            controller.onGeneratePdf();
        });
        generatePdfButton.setEnabled(canGenerate);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 8;
        c.gridwidth = 2;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        add(generatePdfButton, c);
    }

    private void setupAddTaskButton() {
        generatePdfButton = new JButton("Añadir Tarea");
        generatePdfButton.addActionListener(e -> {
            addTask();
        });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 5;
        c.gridwidth = 4;
        c.weighty = 0.01f;
        c.insets = getCommonInsets();
        add(generatePdfButton, c);
    }

    private void setupAddTaskTable() {
        List<Task> tasks = controller.getModel().getTasks();
        if(taskComponent != null) {
            remove(taskComponent);
        }
        DefaultTableModel defaultModel = new DefaultTableModel();
        defaultModel.addColumn("Título");
        defaultModel.addColumn("Descripción");
        defaultModel.addColumn("Precio");
        defaultModel.addColumn("");
        defaultModel.addColumn("");
        for(int i = 0; i < tasks.size(); i++) {
            Object[] rowData = new Object[5];
            rowData[0] = tasks.get(i).getTitle();
            rowData[1] = tasks.get(i).getDescription();
            rowData[2] = tasks.get(i).getPrice();
            rowData[3] = "Buscar";
            rowData[4] = "Eliminar";
            defaultModel.addRow(rowData);
        }

        taskTable = new AutoRowHeightTable(defaultModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.getColumnModel().getColumn(0).setMinWidth(100);
        taskTable.getColumnModel().getColumn(1).setMinWidth(100);
        taskTable.getColumnModel().getColumn(2).setMinWidth(100);
        taskTable.getColumnModel().getColumn(2).setMaxWidth(100);
        taskTable.getColumnModel().getColumn(3).setMinWidth(100);
        taskTable.getColumnModel().getColumn(3).setMaxWidth(100);
        taskTable.getColumnModel().getColumn(4).setMinWidth(100);
        taskTable.getColumnModel().getColumn(4).setMaxWidth(100);
        taskTable.setDefaultRenderer(Object.class, new JTextAreaCellRenderer());
        
        TableCellEditor editor = taskTable.getDefaultEditor(Object.class);
        editor.addCellEditorListener(new CellEditorListener() {
            @Override 
            public void editingCanceled(ChangeEvent e) {
            }
         
            @Override
            public void editingStopped(ChangeEvent e) {
                validateChanges();
            }
         });
        taskTable.setDefaultEditor(Object.class, editor);
        taskTable.setRowSelectionAllowed(true);
        
        JScrollPane scrollPane = new  JScrollPane(taskTable);
        taskTable.setFillsViewportHeight(true);        
    
        new ButtonColumn(taskTable, 3, e -> {
            new TaskSelectDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Elegir Tarea", t -> {
                ((DefaultTableModel)taskTable.getModel()).setValueAt(t.getTitle(), taskTable.getSelectedRow(), 0);
                ((DefaultTableModel)taskTable.getModel()).setValueAt(t.getDescription(), taskTable.getSelectedRow(), 1);
                ((DefaultTableModel)taskTable.getModel()).setValueAt(t.getPrice(), taskTable.getSelectedRow(), 2);
            }, true);
        });
        new ButtonColumn(taskTable, 4, e -> {
            new ConfirmDialog(new JFrame(), "Eliminar tarea", "¿Seguro que quieres eliminar la tarea? Esta acción no se puede revertir.", true, () -> {
                ((DefaultTableModel)taskTable.getModel()).removeRow(taskTable.getSelectedRow());
            });                
        });
                
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 6;
        c.gridwidth = 4;
        c.weighty = 1f;
        c.insets = getCommonInsets();
        add(scrollPane, c);
    }

    private void addTask() {
        ((DefaultTableModel) taskTable.getModel()).addRow(new Object[]{"", "", "", "Buscar", "Eliminar"});
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

    public void validateChanges() {
        String id = billIdField.getText();
        String date = billDateField.getText();
        String project = billProjectField.getText();
        boolean hasErrors = id.isEmpty() || date.isEmpty() || project.isEmpty();

        if (hasErrors) {
            canSave = false;
            canGenerate = false;
        } else {
            boolean modified = !id.equals(controller.getModel().getId()) || 
                                !date.equals(controller.getModel().getDate()) || 
                                !project.equals(controller.getModel().getProject());
            if(!modified) {
                modified = hasDifferentTasks();
            }
            canSave = modified;
            canGenerate = !modified;
        }
        submitButton.setEnabled(canSave);
        generatePdfButton.setEnabled(canGenerate);
    }

    boolean hasDifferentTasks () {
        List<Task> tasks = controller.getModel().getTasks();
        if(tasks.size() != taskTable.getModel().getRowCount()) {
            return true;
        }
        boolean equal = true;
        for(int i = 0; i < tasks.size() && equal; i++) {
            equal &= tasks.get(i).getTitle().equals(taskTable.getValueAt(i, 0));
            equal &= tasks.get(i).getDescription().equals(taskTable.getValueAt(i, 1));
            equal &= tasks.get(i).getPrice() == (double) taskTable.getValueAt(i, 2);
        }
        return !equal;
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
}
