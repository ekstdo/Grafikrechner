package grafikrechner.ui;

import grafikrechner.parser.Parser;
import grafikrechner.parser.Term;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    public PlotterPanel plotterPanel;
    public TermPanel termPanel;
    Parser p;

    ArrayList<Term> functions;


    public MainWindow(){
        // change the font size
        UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 20)));
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 20)));
        UIManager.put("TextField.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 20)));

        plotterPanel = new PlotterPanel(this);
        termPanel = new TermPanel(this);
        functions = new ArrayList<Term>();

        setLayout(new GridLayout(0, 2));

        add(plotterPanel);
        add(new JScrollPane(termPanel));

        pack();
        plotterPanel.initialCallback();
        setVisible(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        p = new Parser();
    }


    public void addFunction(String s) {
        try {
            functions.add(p.parse(s));
        } catch (ParseException e) {
            showParseError(e);
            functions.add(null);
        }
    }

    private void showParseError(ParseException e) {
        System.out.println(e); // TODO
    }

    public void changeFunction(String s, int index){
        try {
            functions.set(index, p.parse(s));
        } catch (ParseException e) {
            showParseError(e);
        }
    }

    public void removeFunction(int index) {
        functions.remove(index);
    }
}
