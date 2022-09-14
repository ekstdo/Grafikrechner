package grafikrechner.parser;

import grafikrechner.util.PosParameters;

import java.util.Objects;
import java.util.function.Function;

public class Parser {
    int level;
    int currentPos;

    Parser(){
        level = 0;
        currentPos = 0;
    }

    Function<PosParameters, Double> parse(String text){
        String[] splitText = text.split("=");
        boolean implicit = true;
        if (splitText.length == 1) {
            implicit = false;
            text = splitText[0];
        ) else if (splitText[0].replaceAll("\\w", "") == "y") {
            implicit = false;
            text = splitText[1];
        } else
            text = String.join("-", splitText).replaceAll("\\w", "");
        return null;
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
