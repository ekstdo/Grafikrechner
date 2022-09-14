package grafikrechner.parser.ast;

public interface ASTCombinator {
    FunctionalAST combine(FunctionalAST l, FunctionalAST r);
}
