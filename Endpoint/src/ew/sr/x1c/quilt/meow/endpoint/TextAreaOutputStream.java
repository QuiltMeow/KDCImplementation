package ew.sr.x1c.quilt.meow.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {

    private final ByteArrayOutputStream buffer;
    private final JTextArea textArea;

    protected TextAreaOutputStream(JTextArea textArea) {
        super();
        buffer = new ByteArrayOutputStream();
        this.textArea = textArea;
    }

    @Override
    public void flush() throws IOException {
        textArea.append(buffer.toString("UTF-8"));
        buffer.reset();
    }

    @Override
    public void write(int byteData) {
        buffer.write(byteData);
    }

    @Override
    public void write(byte[] byteData, int offset, int length) {
        buffer.write(byteData, offset, length);
    }
}
