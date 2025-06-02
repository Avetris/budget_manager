package com.avetris.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.avetris.listeners.ITaskSelectListener;
import com.avetris.managers.TasksManager;
import com.avetris.models.Task;
import com.avetris.ui.components.ButtonColumn;

public class TaskSelectDialog extends JDialog {

    final String[] COLUMN_NAMES = { "Id", "Titulo", "Descripción", "Precio", ""};

    ITaskSelectListener listener;

    JPanel tablePanel;

    public TaskSelectDialog(JFrame parent, String title, ITaskSelectListener listener, boolean modal) {
        super(parent, title, modal);
        this.listener = listener;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        addTopBar();
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
        add(tablePanel, BorderLayout.CENTER);
        updateView(TasksManager.getInstance().getTasks(), false);

        setVisible(true);
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
            updateView(TasksManager.getInstance().filterTask(filter.getText()), true);
        });
        panel.add(filterButton);
        add(panel, BorderLayout.NORTH);
    }

    public void updateView(List<Task> tasks, boolean withFilter) {
        tablePanel.removeAll();
        
        Object[][] data = new Object[tasks.size()][5];
        for(int i = 0; i < tasks.size(); i++) {
            data[i][0] = tasks.get(i).getId();
            data[i][1] = tasks.get(i).getTitle();
            data[i][2] = tasks.get(i).getDescription();
            data[i][3] = tasks.get(i).getPrice() + "€";
            data[i][4] = "Elegir";
        }
        
        if(data.length > 0) {
            JTable table = new JTable(data, COLUMN_NAMES);
            table.setAutoCreateRowSorter(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getColumnModel().getColumn(0).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMaxWidth(100);
            table.getColumnModel().getColumn(4).setMaxWidth(100);
            table.getColumnModel().getColumn(4).setMinWidth(100);
            table.setDefaultEditor(Object.class, null);
            table.setFocusable(false);
            table.setRowSelectionAllowed(true);
            
            table.setAutoscrolls(true);
    
            JScrollPane scrollPane = new  JScrollPane(table);
            table.setFillsViewportHeight(false); 
    
            new ButtonColumn(table, 4, e -> {
                listener.onSelect(tasks.get(table.getSelectedRow()));
                dispose();
            });
            //Agregamos el JScrollPane al contenedor
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel noContentLabel = new JLabel(withFilter ? "No existen trabajos con el filtro actual." : "No has añadido ningun trabajo todavía.");            
            noContentLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            noContentLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
            tablePanel.add(noContentLabel, BorderLayout.NORTH);
        }
    }
}