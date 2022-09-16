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

    static final double EPSILON = 0.001;

    @Override
    public Quadtree reduce(FunctionalAST f) {
        topLeft = topLeft.reduce(f);
        topRight = topRight.reduce(f);
        bottomLeft = bottomLeft.reduce(f);
        bottomRight = bottomRight.reduce(f);

        // potential optimization
        if (topLeft.isLeaf() && topRight.isLeaf() && bottomRight.isLeaf() && bottomLeft.isLeaf()){
            Leaf topLeftL = (Leaf) topLeft;
            Leaf topRightL = (Leaf) topRight;
            Leaf bottomLeftL = (Leaf) bottomLeft;
            Leaf bottomRightL = (Leaf) bottomRight;

            topLeftL.cacheValues(f);
            topRightL.cacheValues(f);
            bottomLeftL.cacheValues(f);
            bottomRightL.cacheValues(f);

            double topVal = (topLeftL.cachedTLVal + topRightL.cachedTRVal) / 2.0;
            double bottomVal = (bottomRightL.cachedBRVal + bottomLeftL.cachedBLVal) / 2.0;
            double topScore = Math.abs(topLeftL.cachedTRVal - topVal);
            double rightScore = Math.abs(topRightL.cachedBRVal - (bottomRightL.cachedBRVal + topRightL.cachedTRVal) / 2.0);
            double bottomScore = Math.abs(bottomLeftL.cachedBRVal - bottomVal);
            double leftScore = Math.abs(bottomLeftL.cachedTLVal - (topLeftL.cachedTLVal + bottomLeftL.cachedBLVal) / 2.0);
            double centerScore = Math.abs(topRightL.cachedBLVal - (topVal + bottomVal) / 2.0);

            if (topScore < EPSILON && bottomScore < EPSILON && rightScore < EPSILON && leftScore < EPSILON && centerScore < EPSILON){
                Leaf returning = new Leaf(topLeftL.xmin, topLeftL.ymin, bottomRightL.xmax, bottomRightL.ymax);
                returning.cacheValues(f);
                return returning.reduce(f);
            }
        }

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

    @Override
    public boolean isLeaf() {
        return false;
    }
}
