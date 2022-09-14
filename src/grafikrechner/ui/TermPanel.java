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
            row.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();

            JTextField textField = new JTextField("y = x", 10);
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;
            row.add(new JLabel("Term "+ counter + ": "),c);

            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 2;
            row.add(textField, c);

            c.gridx = 2;
            c.gridy = 0;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.5;
            JButton deleteButton = new JButton("-");
            deleteButton.addActionListener(delEv -> {
                this.remove(row);
                plotter.pack();
            });
            row.add(deleteButton, c);
            add(row);
            plotter.pack();
        });

        add(addButton);
    }
}
