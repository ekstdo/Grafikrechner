package grafikrechner.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TermRow extends JPanel {
    JTextField textField;
    JLabel errorLabel;
    Highlighter highlighter;
    Highlighter.HighlightPainter hlpainter;

    TermRow(TermPanel panel, MainWindow plotter){
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        textField = new JTextField("y = x", 10);
        errorLabel = new JLabel("");
        highlighter = textField.getHighlighter();
        hlpainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(247, 47, 53));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }

            public void update(DocumentEvent e) {
                highlighter.removeAllHighlights();
                plotter.changeFunction(textField.getText(), panel.getIndex(TermRow.this));
                plotter.plotterPanel.repaint();
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        add(new JLabel("Term "+ panel.counter + ": "),c);

        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2;
        add(textField, c);

        c.gridx = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        JButton deleteButton = new JButton("-");
        deleteButton.addActionListener(delEv -> {
            plotter.removeFunction(panel.getIndex(TermRow.this));
            panel.remove(this);
            plotter.pack();
        });
        add(deleteButton, c);
    }

    public void markError(int index, int len){
        highlighter.removeAllHighlights();
        try {
            highlighter.addHighlight(index, index + len, hlpainter);
        } catch (BadLocationException e){
            System.out.println(e);
        }
    }
}
