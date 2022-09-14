package grafikrechner.ui;

import javax.swing.*;
import java.awt.*;

public class TermPanel extends JPanel {
    int counter = 0;
    TermPanel(MainWindow plotter){
        JButton addButton = new JButton("+");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addButton.addActionListener(ev -> {
            counter += 1;
            JPanel row = new JPanel();
            row.setLayout(new GridLayout(0, 3));
            JTextField textField = new JTextField("y = x", 10);
            row.add(new JLabel("Term "+ counter + ": "));
            row.add(textField);
            JButton deleteButton = new JButton("-");
            deleteButton.addActionListener(delEv -> {
                this.remove(row);
                plotter.pack();
            });
            row.add(deleteButton);
            add(row);
            plotter.pack();
        });

        add(addButton);
    }
}
