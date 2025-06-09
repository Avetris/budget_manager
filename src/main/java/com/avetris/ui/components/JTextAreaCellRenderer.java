package com.avetris.ui.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View; // Important for calculating accurate wrapped height

public class JTextAreaCellRenderer  extends JTextArea implements TableCellRenderer {

    // Use a static Border to avoid creating new objects constantly for performance
    private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    public JTextAreaCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true); // Wrap at word boundaries
        setOpaque(true); // Essential for background to be painted correctly
        setBorder(NO_FOCUS_BORDER); // Add a small border for padding
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        // Set the text for the renderer
        setText(value == null ? "" : value.toString());

        // Set background and foreground colors based on selection state
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        // Set font to match table's font
        setFont(table.getFont());

        // The key to getting the correct preferred height:
        // You MUST set the width of the JTextArea here.
        // If you don't, getPreferredSize() will return a height assuming infinite width
        // (i.e., one very long line).
        // We set the height to MAX_VALUE because we want to allow it to grow vertically.
        // The actual column width is only available when the renderer is being prepared.
        int columnWidth = table.getColumnModel().getColumn(column).getWidth();
        setSize(new Dimension(columnWidth, Integer.MAX_VALUE));

        // IMPORTANT: The preferred size of this component (the JTextArea)
        // after setting its text and sizing it will represent the actual
        // height needed for the wrapped text. This value will be used by
        // the table to adjust the row height.
        // We do NOT call table.setRowHeight here directly. That will lead to
        // infinite loops/performance issues.

        return this;
    }

    /**
     * Calculates the number of displayed (wrapped) lines for the current text
     * and set width. This is useful for debugging or if you explicitly need the count.
     * It's not directly used for setting row height, as getPreferredSize().height
     * is more direct.
     * @return The number of visual lines.
     */
    public int getDisplayedLineCount() {
        if (!getLineWrap()) {
            return getLineCount(); // For non-wrapped text, this is enough
        }
        // Access the View associated with the JTextArea's Document
        View view = getUI().getRootView(this).getView(0);
        if (view != null) {
            return view.getViewCount(); // Each child view in a ParagraphView is a visual line
        }
        return 0;
    }
}