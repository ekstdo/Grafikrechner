package grafikrechner.parser;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.util.PosParameters;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Term {
    boolean implicit;
    public FunctionalAST fun;

    Term(boolean implicit, FunctionalAST fun){
        this.implicit = implicit;
        this.fun = fun;
    }

    public boolean is_implicit() {
        return implicit;
    }
}
