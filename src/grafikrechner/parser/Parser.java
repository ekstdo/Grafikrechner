package grafikrechner.parser;

import grafikrechner.parser.ast.ASTCombinator;
import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.parser.ast.FunctionalASTCombinator;
import grafikrechner.util.Pair;
import grafikrechner.util.PosParameters;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;

public class Parser {
    Lexer lex;
    String text = "";
    Map<String,
            ASTCombinator> binOp;
    Map<String, Pair<Integer, Integer>> binPrec;
    Map<String, Function<ArrayList<FunctionalAST>, FunctionalAST>> functions;

    Map<String, FunctionalAST> variables;
    Token save = null;

    public Parser(){
        binPrec = new HashMap<>();
        binPrec.put("+", new Pair<>(1, 2));
        binPrec.put("-", new Pair<>(1, 2));
        binPrec.put("*", new Pair<>(3, 4));
        binPrec.put("/", new Pair<>(3, 4));
        binPrec.put("^", new Pair<>(6, 5));

        binOp = new HashMap<>();
        binOp.put("+", new FunctionalASTCombinator(Double::sum));
        binOp.put("-", new FunctionalASTCombinator((x, y) -> x - y));
        binOp.put("*", new FunctionalASTCombinator((x, y) -> x * y));
        binOp.put("/", new FunctionalASTCombinator((x, y) -> x / y));
        binOp.put("^", new FunctionalASTCombinator(Math::pow));

        functions = new HashMap<>();
        functions.put("sin", args -> (p -> Math.sin(args.get(0).apply(p))));
        functions.put("cos", args -> (p -> Math.cos(args.get(0).apply(p))));
        functions.put("tan", args -> (p -> Math.tan(args.get(0).apply(p))));

        variables = new HashMap<>();
    }

    public static void main(String[] args) {
        try {
            Parser p = new Parser();
            Term t = p.parse("5 + 4 * x ^ 3 * y + y * x  + x ^ 0.5  ");
            System.out.println(t.fun.apply(new PosParameters(4, 5)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Term parse(String text) throws ParseException {
        save = null;
        String[] splitText = text.split("=");
        boolean implicit = true;
        int errorOffset = 0;
        if (splitText.length > 2)
            throw new ParseException("too many = signs", splitText[0].length() + splitText[1].length());
        if (splitText.length == 1) {
            implicit = false;
            text = splitText[0];
        } else if (splitText[0].replaceAll("\\W", "").equals("y")) {
            implicit = false;
            errorOffset = splitText[0].length();
            text = splitText[1];
        } else
            text = String.join("-", splitText);
        lex = new Lexer(text);
        this.text = text;
        try {
            return new Term(implicit, parseExpr(0));
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset() + errorOffset);
        }
    }

    FunctionalAST parseExpr(int min_prec) throws ParseException{
        FunctionalAST left;
        Token nextToken = getToken();
        Optional<Double> value = nextToken.constantValue();
        if (value.isPresent()) {
            left = p -> value.get();
        } else if (nextToken.type == TokenType.PAREN_LEFT) {
            int pos = nextToken.position;
            left = parseExpr(0);
            nextToken = getToken();
            if (nextToken.type != TokenType.PAREN_RIGHT)
                throw new ParseException("unclosed parenthesis", pos);
        } else if (nextToken.type == TokenType.X) {
            left = p -> p.x;
        } else if (nextToken.type == TokenType.Y) {
            left = p -> p.y;
        } else if (nextToken.type == TokenType.R) {
            left = p -> p.rCached;
        } else if (nextToken.type == TokenType.PHI) {
            left = p -> p.phiCached;
        } else if (nextToken.type == TokenType.VARIABLE) {
            String var = nextToken.inner;
            Token mbyParen = getToken();
            if (mbyParen.type == TokenType.PAREN_LEFT) {
                ArrayList<FunctionalAST> args = new ArrayList<>();

                mbyParen = getToken();
                while (mbyParen.type != TokenType.PAREN_RIGHT){
                    save = mbyParen;
                    args.add(parseExpr(0));
                    mbyParen = getToken();
                }

                Function<ArrayList<FunctionalAST>, FunctionalAST> function = functions.get(var);
                if (function == null){
                    throw new ParseException("unknown function", save.position);
                }
                left = function.apply(args);
            } else {
                save = mbyParen;
                FunctionalAST ret = variables.get(var);
                if (ret == null)
                    throw new ParseException("unknown variable", save.position);
                else
                    left = ret;
            }
        } else
            throw new ParseException("invalid begin", nextToken.position);

        while (true) {
            nextToken = getToken();
            save = nextToken;
            if (nextToken.type == TokenType.COMMA || nextToken.type == TokenType.BRACK_RIGHT || nextToken.type == TokenType.PAREN_RIGHT || nextToken.isEnd()){
                break;
            }
            if (nextToken.type != TokenType.OPERATOR) {
                System.out.println(nextToken.type);
                throw new ParseException("expected an operator", nextToken.position);
            }
            Pair<Integer, Integer> p = binPrec.get(nextToken.inner);
            if (p == null){
                throw new ParseException("unknown operator", nextToken.position);
            }

            if (p.first < min_prec) {
                break;
            }

            nextToken = getToken(); // remove save

            FunctionalAST right = parseExpr(p.second);
            left = binOp.get(nextToken.inner).combine(left, right);
        }


        return left;
    }

    Token getToken() throws ParseException {
        if (save != null) {
            Token t = save;
            save = null;
            return t;
        }
        if (lex.hasNext())
            try {
                return lex.next();
            } catch (RuntimeException e) {
                throw new ParseException(e.getMessage(), lex.startPos);
            }
        else
            return new Token(TokenType.END, "", text.length(), 0);
    }
}
