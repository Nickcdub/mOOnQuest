package Controller;

import java.io.OutputStream;

public class BuilderOutputStream extends OutputStream {
    private final StringBuilder myBuilder;
    //Rather than writing to console, instead writes to this string builder
    BuilderOutputStream(StringBuilder theBuilder){
        myBuilder = theBuilder;
    }

    @Override
    public void write(int b) {
    //append characters to builder
    myBuilder.append((char) b);
    }
}
