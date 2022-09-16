package grafikrechner.ui.quadtree;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.ui.PlotterPanel;

import java.awt.*;
import java.util.List;

public class Tree extends Quadtree {
    public Quadtree topLeft;
    public Quadtree topRight;
    public Quadtree bottomLeft;
    public Quadtree bottomRight;

    Tree(Quadtree topLeft, Quadtree topRight, Quadtree bottomRight, Quadtree bottomLeft){
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public Quadtree reduce(FunctionalAST f) {
        topLeft = topLeft.reduce(f);
        topRight = topRight.reduce(f);
        bottomLeft = bottomLeft.reduce(f);
        bottomRight = bottomRight.reduce(f);

        if (topLeft.isEmpty() && topRight.isEmpty() && bottomLeft.isEmpty() && bottomRight.isEmpty())
            return new Empty();

        if (topLeft.isFull() && topRight.isFull()  && bottomLeft.isFull() && bottomRight.isFull())
            return new Full();

        return this;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void draw(FunctionalAST f, PlotterPanel plotterPanel, Graphics g) {
        topLeft.draw(f, plotterPanel, g);
        topRight.draw(f, plotterPanel, g);
        bottomLeft.draw(f, plotterPanel, g);
        bottomRight.draw(f, plotterPanel, g);
    }

    @Override
    public List<Leaf> toList() {
        List<Leaf> returning = topLeft.toList();
        returning.addAll(topRight.toList());
        returning.addAll(bottomLeft.toList());
        returning.addAll(bottomRight.toList());
        return returning;
    }
}
