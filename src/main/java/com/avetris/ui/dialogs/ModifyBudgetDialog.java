package com.avetris.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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
import javax.swing.text.NumberFormatter;

import com.avetris.controllers.BudgetsController;
import com.avetris.models.Budget;
import com.avetris.models.Client;
import com.avetris.models.Task;
import com.avetris.ui.components.AutoRowHeightTable;
import com.avetris.ui.components.ButtonColumn;
import com.avetris.ui.components.JTextAreaCellRenderer;

public class ModifyBudgetDialog extends JDialog implements WindowListener, DocumentListener {

    BudgetsController controller;

    JTextField idField;
    JTextField projectField;
    JTextField dateField;
    JTextField clientNifField;
    JComboBox<String> clientTypeField;
    JTextField clientNameField;   
    JTextField clientAddressField;
    
    JFormattedTextField ivaField;
    JLabel total;
    JLabel totalIva;
    JLabel totalWithIva;

    JComponent taskComponent;
    AutoRowHeightTable taskTable;

    JButton submitButton;
    JButton generatePdfButton;

    boolean canGenerate = false;
    boolean canSave = false;

    public ModifyBudgetDialog(JFrame parent, String title, BudgetsController controller, boolean modal) {
        super(parent, title, modal);

        this.controller = controller;

        setSize(1024, 800);
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
        idField = new JTextField(id);
        label.setLabelFor(idField);
        idField.setEditable(id == null || id.isEmpty());
        idField.getDocument().addDocumentListener(this);
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
        add(idField, c);
        canGenerate = id != null && !id.isEmpty();
    }

    public void setupDate() {
        JLabel label = new JLabel();
        label.setText("Fecha");
        dateField = new JTextField(controller.getModel().getDate());
        label.setLabelFor(dateField);
        dateField.getDocument().addDocumentListener(this);
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
        add(dateField, c); 
    }

    public void setupProject() {
        JLabel label = new JLabel();
        label.setText("Proyecto");
        projectField = new JTextField(controller.getModel().getProject());
        label.setLabelFor(projectField);
        projectField.getDocument().addDocumentListener(this);
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
        add(projectField, c); 
    }

    public void setupClientNif() {
        JLabel label = new JLabel();
        label.setText("NIF/DNI");
        clientNifField = new JTextField(controller.getModel().getClient().getNif());
        label.setLabelFor(clientNifField);
        clientNifField.getDocument().addDocumentListener(this);
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
        add(clientNifField, c);
    }

    public void setupClientType() {
        clientTypeField = new JComboBox<>();
        clientTypeField.addItem("Empresa");
        clientTypeField.addItem("Individual");
        clientTypeField.setSelectedItem(controller.getModel().getClient().isCompany() ? "Empresa" : "Individual");
        clientTypeField.addActionListener(l -> validateChanges());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 2;
        c.weighty = 0.05f;
        c.insets = getCommonInsets();
        c.weightx = 0.5;
        add(clientTypeField, c); 
    }


    public void setupClientName() {
        JLabel label = new JLabel();
        label.setText("Nombre Cliente");
        clientNameField = new JTextField(controller.getModel().getClient().getName());
        label.setLabelFor(clientNameField);
        clientNameField.getDocument().addDocumentListener(this);
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
        add(clientNameField, c);
    }

    public void setupClientAddress() {
        JLabel label = new JLabel();
        label.setText("Dirección Cliente");
        clientAddressField = new JTextField(controller.getModel().getClient().getAddress());
        label.setLabelFor(clientAddressField);
        clientAddressField.getDocument().addDocumentListener(this);
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
        add(clientAddressField, c);
    }

    public void setupIvaTotal() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 7;
        c.weighty = 0.05f;
        c.weightx = 0.05;
        c.insets = getCommonInsets();

        JLabel totalLabel = new JLabel();
        totalLabel.setText("Total");
        c.gridx = 2;
        add(totalLabel, c);
        total = new JLabel(controller.getModel().getTotal() + "");
        c.gridx = 3;
        add(total, c);

        
        JLabel ivaLabel = new JLabel();
        ivaLabel.setText("IVA (%)");
        c.gridy++;
        c.gridx = 0;
        add(ivaLabel, c);        
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getNumberInstance());
        formatter.setMinimum(0.0);
        formatter.setMaximum(100.0);
        formatter.setAllowsInvalid(false);
        ivaField = new JFormattedTextField(formatter);
        ivaField.setValue(controller.getModel().getIva());

        ivaLabel.setLabelFor(ivaField);
        ivaField.getDocument().addDocumentListener(this);
        c.gridx = 1;
        add(ivaField, c);
        JLabel ivaPriceLabel = new JLabel();
        ivaPriceLabel.setText("Total IVA");
        c.gridx = 2;
        add(ivaPriceLabel, c);
        totalIva = new JLabel(controller.getModel().getTotalIva() + "");
        c.gridx = 3;
        add(totalIva, c);
        JLabel totalWithIvaLabel = new JLabel();
        totalWithIvaLabel.setText("Total (Con IVA)");
        c.gridy++;
        c.gridx = 2;
        add(totalWithIvaLabel, c);
        totalWithIva = new JLabel(controller.getModel().getTotalWithIva() + "");
        c.gridx = 3;
        add(totalWithIva, c);
    }

    private void setupSaveButton() {
        submitButton = new JButton("Guardar");
        submitButton.addActionListener(e -> {
            List<Task> tasks = new java.util.ArrayList<>();
            for(int i = 0; i < taskTable.getModel().getRowCount(); i++) {
                double price = 0;
                try {
                    price += (Double) taskTable.getValueAt(i, 2);
                } catch (NumberFormatException e1) {}
                tasks.add(new Task((String) taskTable.getValueAt(i, 0), (String) taskTable.getValueAt(i, 1), price));
            }
            boolean success = controller.onSubmit(
                new Budget(
                    idField.getText().trim(),
                    projectField.getText().trim(),
                    dateField.getText().trim(),
                    new Client(
                        clientNifField.getText().trim(),
                        clientTypeField.getSelectedItem().equals("Empresa"), 
                        clientNameField.getText().trim(), 
                        clientAddressField.getText().trim()),
                    tasks
            ));
            if(success) {
                canGenerate = true;
                canSave = false;
                generatePdfButton.setEnabled(canGenerate);
                submitButton.setEnabled(canSave);
            } else {
                new InfoDialog(new JFrame(), "Existe ID", "Ya existe un presupuesto con el id insertado. Cambia el ID y vuelve a intentarlo.", true);
            }
        });
        submitButton.setEnabled(canSave);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 10;
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
        c.gridy = 10;
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
            rowData[2] = Double.valueOf(tasks.get(i).getPrice());
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
        taskTable.setDefaultRenderer(String.class, new JTextAreaCellRenderer());
        
        TableCellEditor editor = taskTable.getDefaultEditor(Object.class);
        editor.addCellEditorListener(new CellEditorListener() {
            @Override 
            public void editingCanceled(ChangeEvent e) {
            }
         
            @Override
            public void editingStopped(ChangeEvent e) {
                for(int i = 0; i < taskTable.getModel().getRowCount(); i++) {
                    taskTable.getModel().setValueAt(((String)taskTable.getValueAt(i, 0)).toUpperCase(), i, 0);
                }
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
                validateChanges();
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
        ((DefaultTableModel) taskTable.getModel()).addRow(new Object[]{"", "", Double.valueOf(0), "Buscar", "Eliminar"});
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
        String id = idField.getText();
        String date = dateField.getText();
        String project = projectField.getText();
        boolean hasErrors = id.isEmpty() || date.isEmpty() || project.isEmpty();
        calculatePrices();


        Budget model = controller.getModel();
        if (hasErrors) {
            canSave = false;
            canGenerate = false;
        } else {
            boolean modified = !id.equals(model.getId()) || 
                                !date.equals(model.getDate()) || 
                                !project.equals(model.getProject()) || 
                                !clientNifField.getText().equals(model.getClient().getNif()) || 
                                !clientNameField.getText().equals(model.getClient().getName()) || 
                                clientTypeField.getSelectedItem().equals("Empresa") != model.getClient().isCompany() || 
                                !clientAddressField.getText().equals(model.getClient().getAddress());
            if(!modified) {
                modified = hasDifferentTasks();
            }
            canSave = modified;
            canGenerate = !modified;
        }
        submitButton.setEnabled(canSave);
        generatePdfButton.setEnabled(canGenerate);
    }

    void calculatePrices() {  
        double totalValue = 0;
        for(int i = 0; i < taskTable.getModel().getRowCount(); i++) {
            try {
                totalValue += (Double) taskTable.getValueAt(i, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double totalIVAValue = 0;
        try {
            totalIVAValue = totalValue * (Double.parseDouble(ivaField.getText()) / 100.0);
        }catch(Exception e) {
            e.printStackTrace();
        }
        total.setText(totalValue + "€");
        totalIva.setText(totalIVAValue + "€");
        totalWithIva.setText((totalValue + totalIVAValue) + "€");
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
