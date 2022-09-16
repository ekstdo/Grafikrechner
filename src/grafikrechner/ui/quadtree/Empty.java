package grafikrechner.ui.quadtree;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.ui.PlotterPanel;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Empty extends Quadtree {
    @Override
    public Quadtree reduce(FunctionalAST f) {
        return this;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void draw(FunctionalAST f, PlotterPanel plotterPanel, Graphics g) {

    }

    @Override
    public List<Leaf> toList() {
        return new LinkedList<>();
    }
}
