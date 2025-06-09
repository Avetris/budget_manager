package com.avetris.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import com.avetris.listeners.IBudgetListener;
import com.avetris.models.Budget;
import com.avetris.ui.components.ButtonColumn;
import com.avetris.ui.dialogs.ConfirmDialog;

public class BudgetsTab extends JPanel   {

    final String[] COLUMN_NAMES = { "Id", "Proyecto", "Cliente", "Fecha", "", "", ""};

    IBudgetListener listener;

    JPanel tablePanel;

    public BudgetsTab() {
        setLayout(new BorderLayout());
        addTopBar();
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
        add(tablePanel, BorderLayout.CENTER);
        updateView(new Budget[0], false);
    }

    public void addListener(IBudgetListener listener) {
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
        JButton button = new JButton("Crear Presupuesto");
        button.addActionListener(e -> {
            listener.showDialog(null);
        });
        panel.add(button);
        add(panel, BorderLayout.NORTH);
    }

    public void updateView(Budget[] budgets, boolean withFilter) {
        tablePanel.removeAll();
        
        Arrays.sort(budgets, (a,b) -> {
            return a.getDate().compareTo(b.getDate());
        });
        Object[][] data = new Object[budgets.length][7];
        for(int i = 0; i < budgets.length; i++) {
            data[i][0] = budgets[i].getId();
            data[i][1] = budgets[i].getProject();
            data[i][2] = budgets[i].getClient().getName();
            data[i][3] = budgets[i].getDate();
            data[i][4] = "Editar";
            data[i][5] = "Generar PDF";
            data[i][6] = "Eliminar";
        }
        
        if(data.length > 0) {
            JTable table = new JTable(data, COLUMN_NAMES);
            table.setAutoCreateRowSorter(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getColumnModel().getColumn(0).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMinWidth(100);
            table.getColumnModel().getColumn(3).setMaxWidth(100);
            table.getColumnModel().getColumn(4).setMinWidth(100);
            table.getColumnModel().getColumn(4).setMaxWidth(100);
            table.getColumnModel().getColumn(5).setMinWidth(120);
            table.getColumnModel().getColumn(5).setMaxWidth(120);
            table.getColumnModel().getColumn(6).setMinWidth(100);
            table.getColumnModel().getColumn(6).setMaxWidth(100);
            table.setDefaultEditor(Object.class, null);
            table.setFocusable(false);
            table.setRowSelectionAllowed(true);
            
            table.setAutoscrolls(true);

            table.setRowSorter(new TableRowSorter(table.getModel()) {
                @Override
                public boolean isSortable(int column) {
                    return column < 4;
                }
            });

            table.getTableHeader().setReorderingAllowed(false);
    
            JScrollPane scrollPane = new  JScrollPane(table);
            table.setFillsViewportHeight(false); 
    
            new ButtonColumn(table, 4, e -> {                       
                listener.showDialog(table.getValueAt(table.getSelectedRow(), 0).toString());
            });
            new ButtonColumn(table, 5, e -> {            
                listener.onGeneratePdf(table.getValueAt(table.getSelectedRow(), 0).toString());
            });

            new ButtonColumn(table, 6, e -> {
                new ConfirmDialog(new JFrame(), "Eliminar presupuesto", "¿Seguro que quieres eliminar el presupuesto? Esta acción no se puede revertir.", true, () -> {
                    listener.onRemoveBudget(table.getValueAt(table.getSelectedRow(), 0).toString());
                });                
            });
            //Agregamos el JScrollPane al contenedor
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel noContentLabel = new JLabel(withFilter ? "No existen presupuestos con el filtro actual." : "No has creado ningun presupuesto todavía.");            
            noContentLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            noContentLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
            tablePanel.add(noContentLabel, BorderLayout.NORTH);
        }
        updateUI();
    }
}