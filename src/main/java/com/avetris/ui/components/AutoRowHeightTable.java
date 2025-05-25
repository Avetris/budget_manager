package com.avetris.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AutoRowHeightTable extends JTable {

    // Cache to store calculated row heights to avoid redundant calculations
    // This can help performance for large tables, but needs to be cleared
    // if data or column widths change.
    private final Map<Integer, Integer> rowHeights = new HashMap<>();

    public AutoRowHeightTable(DefaultTableModel model) {
        super(model);

        // Add a TableColumnModelListener to recalculate heights when column widths change
        getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {}
            @Override
            public void columnRemoved(TableColumnModelEvent e) {}
            @Override
            public void columnMoved(TableColumnModelEvent e) {}

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                // Column width changed, so all wrapped heights might be invalid
                clearRowHeightCache();
                // Re-validate and repaint to trigger re-calculation of heights
                revalidate();
                repaint();
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {}
        });
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        // This method is called by the JTable to get the component that will render a cell.
        // It's the ideal place to calculate the preferred height of the renderer.

        Component c = super.prepareRenderer(renderer, row, column);

        // Calculate and set the row height only if it's a JTextArea and needs adjustment
        if (c instanceof JTextArea) {
            JTextArea textArea = (JTextArea) c;

            // Get the preferred height of the JTextArea *after* it has been sized
            // by the renderer's getTableCellRendererComponent method.
            int preferredHeight = textArea.getPreferredSize().height;

            // Add some padding, adjust as needed
            preferredHeight += 4; // Extra padding for aesthetic

            // Get the current row height
            int currentRowHeight = getRowHeight(row);

            // Update row height only if it's different and larger than default or existing
            // This check prevents infinite loops and unnecessary repaints.
            // Also, consider the minimum row height of other cells in the same row.
            // A more robust solution might store preferred heights for ALL cells in a row
            // and take the maximum. For simplicity here, we assume the JTextArea column
            // is the primary driver of height.
            if (preferredHeight != currentRowHeight) {
                // To avoid infinite loop (if this setter causes a re-render that calls this method again)
                // we only set if the height actually changes.
                // For optimal performance, you might store these heights in a map and
                // only call setRowHeight once after all cells in a row are prepared,
                // or after all rows are initially calculated.
                // However, this approach within prepareRenderer is common and generally works
                // for moderate table sizes.

                // Using a cache to optimize, if already calculated for this row and column
                if (!rowHeights.containsKey(row) || rowHeights.get(row) < preferredHeight) {
                     rowHeights.put(row, preferredHeight);
                     setRowHeight(row, preferredHeight);
                }
            }
        }
        return c;
    }

    // Call this method whenever table data changes or for a full recalculation
    public void updateRowHeights() {
        clearRowHeightCache(); // Clear cache before full recalculation
        for (int row = 0; row < getRowCount(); row++) {
            int rowHeight = getRowHeight(); // Start with default or current
            for (int column = 0; column < getColumnCount(); column++) {
                Component comp = prepareRenderer(getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            setRowHeight(row, rowHeight);
        }
    }

    private void clearRowHeightCache() {
        rowHeights.clear();
    }
}
