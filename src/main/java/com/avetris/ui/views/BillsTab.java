package com.avetris.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
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

import com.avetris.listeners.IBillListener;
import com.avetris.models.Bill;
import com.avetris.models.Task;
import com.avetris.ui.components.ButtonColumn;
import com.avetris.ui.dialogs.ConfirmDialog;

public class BillsTab extends JPanel   {

    final String[] COLUMN_NAMES = { "Id", "Proyecto", "Cliente", "Fecha", "", ""};

    IBillListener listener;

    JPanel tablePanel;

    public BillsTab() {
        setLayout(new BorderLayout());
        addTopBar();
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
        add(tablePanel, BorderLayout.CENTER);
        updateView(new Bill[0], false);
    }

    public void addListener(IBillListener listener) {
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
        JButton button = new JButton("Crear Factura");
        button.addActionListener(e -> {
            listener.showDialog(null);
        });
        panel.add(button);
        add(panel, BorderLayout.NORTH);
    }

    public void updateView(Bill[] bills, boolean withFilter) {
        tablePanel.removeAll();
        
        Arrays.sort(bills, (a,b) -> {
            return a.getDate().compareTo(b.getDate());
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Object[][] data = new Object[bills.length][6];
        for(int i = 0; i < bills.length; i++) {
            data[i][0] = bills[i].getId();
            data[i][1] = bills[i].getProject();
            data[i][2] = bills[i].getClient().getName();
            data[i][2] = dateFormat.format(bills[i].getDate());
            data[i][4] = "Editar";
            data[i][5] = "Eliminar";
        }
        
        if(data.length > 0) {
            JTable table = new JTable(data, COLUMN_NAMES);
            table.setAutoCreateRowSorter(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getColumnModel().getColumn(0).setMaxWidth(100);
            table.getColumnModel().getColumn(2).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMinWidth(100);
            table.getColumnModel().getColumn(3).setMaxWidth(100);
            table.getColumnModel().getColumn(4).setMinWidth(100);
            table.getColumnModel().getColumn(4).setMaxWidth(100);
            table.getColumnModel().getColumn(5).setMinWidth(100);
            table.getColumnModel().getColumn(5).setMaxWidth(100);
            table.setDefaultEditor(Object.class, null);
            table.setFocusable(false);
            table.setRowSelectionAllowed(true);
            
            table.setAutoscrolls(true);
    
            JScrollPane scrollPane = new  JScrollPane(table);
            table.setFillsViewportHeight(false); 
    
            new ButtonColumn(table, 3, e -> {                       
                listener.showDialog(table.getValueAt(table.getSelectedRow(), 0).toString());
            });
            new ButtonColumn(table, 4, e -> {
                new ConfirmDialog(new JFrame(), "Eliminar factura", "¿Seguro que quieres eliminar la factura? Esta acción no se puede revertir.", true, () -> {
                    listener.onRemoveBill(table.getValueAt(table.getSelectedRow(), 0).toString());
                });                
            });
            //Agregamos el JScrollPane al contenedor
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel noContentLabel = new JLabel(withFilter ? "No existen facturas con el filtro actual." : "No has creado ninguna factura todavía.");            
            noContentLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            noContentLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
            tablePanel.add(noContentLabel, BorderLayout.NORTH);
        }
        updateUI();
    }
}