package grafikrechner.parser;

import grafikrechner.parser.ast.FunctionalAST;

public class Term {
    boolean implicit;
    public FunctionalAST fun;

    Term(boolean implicit, FunctionalAST fun){
        this.implicit = implicit;
        this.fun = fun;
    }

    public boolean isImplicit() {
        return implicit;
    }
}
