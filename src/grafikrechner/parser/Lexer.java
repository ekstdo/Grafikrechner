package grafikrechner.parser;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayDeque;
import java.util.Iterator;

public class Lexer implements Iterator<Token> {
    CharacterIterator it;
    String text;
    int startPos;
    ArrayDeque<Character> charQueue;
    boolean done = false;

    Lexer(String text) {
        this.it = new StringCharacterIterator(text);
        this.text = text;
        startPos = 0;
        this.charQueue = new ArrayDeque<Character>();
        this.charQueue.addFirst(this.it.first());
    }

    Token makeToken(TokenType type){
        int endPos = it.getIndex() - charQueue.size() + 1;
        String subtext = text.substring(startPos, endPos);
        if (subtext.equals("x"))
            type = TokenType.X;
        else if (subtext.equals("y"))
            type = TokenType.Y;
        else if (subtext.equals("e"))
            type = TokenType.E;
        return new Token(type, subtext, startPos, endPos - startPos);
    }

    char getNext(){
        if (charQueue.isEmpty()){
            return it.next();
        } else {
            return charQueue.removeFirst();
        }
    }

    void save(char c){
        if (!charQueue.contains(CharacterIterator.DONE)) {
            charQueue.addLast(c);
        }
    }

    @Override
    public boolean hasNext() {
        char c = getNext();
        if (c != CharacterIterator.DONE) {
            charQueue.addFirst(c);
        }
        return !(c == CharacterIterator.DONE && charQueue.isEmpty());
    }

    @Override
    public Token next() {
        char next = getNext();
        int len;
        Token returning;

        while(Character.isWhitespace(next)) {
            next = getNext();
        }
        save(next);
        next = getNext();
        startPos = it.getIndex() - charQueue.size();

        if (Character.isDigit(next) || next == '.') {
            boolean sep = next == '.';
            while (Character.isDigit(next = getNext()) || next == '.') {
                if (next == '.') {
                    if (sep)
                        throw new RuntimeException("Lex Error: number has multiple dots");
                    else
                        sep = true;
                }
            }
            save(next);
            returning = makeToken(TokenType.NUMBER);
        }
        else if (next == '(') returning = makeToken(TokenType.PAREN_LEFT);
        else if (next == ')') returning = makeToken(TokenType.PAREN_RIGHT);
        else if (next == '[') returning = makeToken(TokenType.BRACK_LEFT);
        else if (next == ']') returning = makeToken(TokenType.BRACK_RIGHT);
        else if (Character.isAlphabetic(next)) {
            while (Character.isAlphabetic(next = getNext())){}
            save(next);
            returning = makeToken(TokenType.VARIABLE);
        } else {
            returning = makeToken(TokenType.OPERATOR);
        }

        next = getNext();
        while(Character.isWhitespace(next)) {
            next = getNext();
        }
        save(next);

        return returning;


    }
}
