package com.avetris.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.avetris.listeners.ITaskListener;
import com.avetris.models.Task;
import com.avetris.ui.components.ButtonColumn;
import com.avetris.ui.dialogs.ConfirmDialog;

public class TasksTab extends JPanel   {

    final String[] COLUMN_NAMES = { "Id", "Titulo", "Precio", "", ""};

    ITaskListener listener;

    JPanel tablePanel;

    public TasksTab() {
        setLayout(new BorderLayout());
        addTopBar();
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
        add(tablePanel, BorderLayout.CENTER);
        updateView(new Task[0], false);
    }

    public void addListener(ITaskListener listener) {
        this.listener = listener;
    }

    private void addTopBar() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JPanel BoxLay = new JPanel();
        BoxLay.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        JTextField filter = new JTextField();
        filter.setColumns( 20 );
        panel.add(filter);
        JButton filterButton = new JButton("Filtrar");
        filterButton.addActionListener(e -> {
            listener.setFilter(filter.getText());
        });
        panel.add(filterButton);
        JButton button = new JButton("Crear Trabajo");
        button.addActionListener(e -> {
            listener.showDialog(-1);
        });
        panel.add(button);
        add(panel, BorderLayout.NORTH);
    }

    public void updateView(Task[] tasks, boolean withFilter) {
        tablePanel.removeAll();
        
        Object[][] data = new Object[tasks.length][5];
        for(int i = 0; i < tasks.length; i++) {
            data[i][0] = tasks[i].getId();
            data[i][1] = tasks[i].getTitle();
            data[i][2] = tasks[i].getPrice() + "€";
            data[i][3] = "Editar";
            data[i][4] = "Eliminar";
        }
        
        if(data.length > 0) {
            JTable table = new JTable(data, COLUMN_NAMES);
            table.setAutoCreateRowSorter(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getColumnModel().getColumn(0).setMaxWidth(100);
            table.getColumnModel().getColumn(2).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMinWidth(100);
            table.getColumnModel().getColumn(4).setMinWidth(100);
            table.getColumnModel().getColumn(4).setMaxWidth(100);
            table.setDefaultEditor(Object.class, null);
            table.setFocusable(false);
            table.setRowSelectionAllowed(true);
            
            table.setAutoscrolls(true);
    
            JScrollPane scrollPane = new  JScrollPane(table);
            table.setFillsViewportHeight(false); 
    
            new ButtonColumn(table, 3, e -> {                       
                listener.showDialog((int) table.getValueAt(table.getSelectedRow(), 0));
            });
            new ButtonColumn(table, 4, e -> {
                new ConfirmDialog(new JFrame(), "Eliminar trabajo", "¿Seguro que quieres eliminar el trabajo? Esta acción no se puede revertir.", true, () -> {
                    listener.onRemoveTask((int) table.getValueAt(table.getSelectedRow(), 0));
                });                
            });
            //Agregamos el JScrollPane al contenedor
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel noContentLabel = new JLabel(withFilter ? "No existen trabajos con el filtro actual." : "No has añadido ningun trabajo todavía.");            
            noContentLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            noContentLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
            tablePanel.add(noContentLabel, BorderLayout.NORTH);
        }
        updateUI();
    }
}