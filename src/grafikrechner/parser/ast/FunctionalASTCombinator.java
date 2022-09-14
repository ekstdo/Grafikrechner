package grafikrechner.parser.ast;

import java.util.function.BiFunction;

public class FunctionalASTCombinator implements ASTCombinator {
    BiFunction<Double, Double, Double> fun;
    public FunctionalASTCombinator(BiFunction<Double, Double, Double> fun){
        this.fun = fun;
    }

    @Override
    public FunctionalAST combine(FunctionalAST l, FunctionalAST r) {
        return p -> this.fun.apply(l.apply(p), r.apply(p));
    }
}
