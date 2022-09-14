package grafikrechner.parser;

import java.util.Optional;

public class Token {
    int position;
    int len;
    String inner;
    TokenType type;

    Token(TokenType type, String inner, int position, int len){
        this.type = type;
        this.position = position;
        this.inner = inner;
        this.len = len;
    }

    boolean isEnd(){
        return this.type == TokenType.END;
    }

    Optional<Double> constantValue(){
        if (type == TokenType.PI) return Optional.of(Math.PI);
        if (type == TokenType.E)  return Optional.of(Math.E);
        if (type == TokenType.NUMBER) return Optional.of(Double.parseDouble(inner));

        return Optional.empty();
    }
}
