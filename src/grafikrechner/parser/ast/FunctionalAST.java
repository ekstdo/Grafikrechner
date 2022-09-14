package grafikrechner.parser.ast;

import grafikrechner.util.PosParameters;

import java.util.function.Function;


public interface FunctionalAST {
    Double apply(PosParameters p);
}
