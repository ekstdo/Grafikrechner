package grafikrechner.parser;

import grafikrechner.parser.ast.ASTCombinator;
import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.parser.ast.FunctionalASTCombinator;
import grafikrechner.util.Pair;
import grafikrechner.util.PosParameters;

import java.text.ParseException;
import java.util.*;

public class Parser {
    int level;
    int currentPos;
    Lexer lex;
    String text = "";
    Map<String,
            ASTCombinator> bin_operators;
    Map<String, Pair<Integer, Integer>> bin_prec;
    Token save = null;

    Parser(){
        level = 0;
        currentPos = 0;

        bin_prec = new HashMap<>();
        bin_prec.put("+", new Pair<>(1, 2));
        bin_prec.put("*", new Pair<>(3, 4));
        bin_prec.put("^", new Pair<>(5, 6));

        bin_operators = new HashMap<>();
        bin_operators.put("+", new FunctionalASTCombinator(Double::sum));
        bin_operators.put("*", new FunctionalASTCombinator((x, y) -> x * y));
        bin_operators.put("^", new FunctionalASTCombinator(Math::pow));
    }

    public static void main(String[] args) {
        try {
            Parser p = new Parser();
            Term t = p.parse("x + y * x");
            System.out.println(t.fun.apply(new PosParameters(4, 5)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    Term parse(String text) throws ParseException {
        String[] splitText = text.split("=");
        boolean implicit = true;
        if (splitText.length > 2)
            throw new ParseException("too many = signs", splitText[0].length() + splitText[1].length());
        if (splitText.length == 1) {
            implicit = false;
            text = splitText[0];
        } else if (splitText[0].replaceAll("\\w", "").equals("y")) {
            implicit = false;
            text = splitText[1];
        } else
            text = String.join("-", splitText);
        lex = new Lexer(text);
        this.text = text;
        return new Term(implicit, parseExpr(0));
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
        } else
            throw new ParseException("invalid begin", nextToken.position);

        while (true) {
            nextToken = getToken();
            save = nextToken;
            if (nextToken.type == TokenType.BRACK_RIGHT || nextToken.type == TokenType.PAREN_RIGHT || nextToken.isEnd()){
                break;
            }
            if (nextToken.type != TokenType.OPERATOR) {
                throw new ParseException("expected an operator", nextToken.position);
            }

            nextToken = getToken(); // remove save
            Pair<Integer, Integer> p = bin_prec.get(nextToken.inner);
            if (p == null){
                throw new ParseException("unknown operator", nextToken.position);
            }

            if (p.first < min_prec) {
                break;
            }

            FunctionalAST right = parseExpr(p.second);
            left = bin_operators.get(nextToken.inner).combine(left, right);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parser parser = (Parser) o;
        return level == parser.level && currentPos == parser.currentPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, currentPos);
    }
}
