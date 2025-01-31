package org.esa.snap.ui.tooladapter.dialogs;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Console-like panel for displaying tool output.
 *
 * @author Cosmin Cara
 */
public class ConsolePane extends JScrollPane {

    private JTextPane textArea;
    private final StringBuilder buffer;

    public ConsolePane() {
        super();
        buffer = new StringBuilder();
        textArea = new JTextPane();
        textArea.setBackground(Color.BLACK);
        textArea.setFont(new Font("Lucida Console", Font.PLAIN, 10));
        textArea.setForeground(Color.WHITE);
        setViewportView(textArea);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setWheelScrollingEnabled(true);
        getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            BoundedRangeModel brm = getVerticalScrollBar().getModel();
            boolean wasAtBottom = true;
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!brm.getValueIsAdjusting()) {
                    if (wasAtBottom)
                        brm.setValue(brm.getMaximum());
                } else
                    wasAtBottom = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());

            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (buffer.length() > 0) {
                    append(buffer.toString());
                }
                buffer.setLength(0);
            }
        });
    }

    /**
     * Appends text to this console.
     *
     * @param text  The text to append
     */
    public void append(String text) {
        if (this.isVisible()) {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
            aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
            int len = textArea.getDocument().getLength();
            textArea.setCaretPosition(len);
            textArea.setCharacterAttributes(aset, false);
            textArea.replaceSelection("\n" + text);
            textArea.repaint();
        } else {
            buffer.append(text);
        }
    }

    /**
     * Clears the contents of this console
     */
    public void clear() {
        textArea.setText("");
    }

}
