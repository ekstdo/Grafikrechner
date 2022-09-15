package grafikrechner.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TermPanel extends JPanel {
    int counter = 0;
    ArrayList<TermRow> rows;
    TermPanel(MainWindow plotter){
        JButton addButton = new JButton("+");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addButton.addActionListener(ev -> {
            counter += 1;
            TermRow row = new TermRow(this, plotter);
            add(row);
            plotter.pack();

            plotter.addFunction("y = x");
        });

        add(addButton);
    }

    public int getIndex(TermRow row){
        return Arrays.asList(getComponents()).indexOf(row) - 1;
    }
}
