package ew.sr.x1c.quilt.meow.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class TextPaneOutputStream extends OutputStream {

    private final ByteArrayOutputStream buffer;
    private final JTextPane textPane;

    protected TextPaneOutputStream(JTextPane textPane) {
        super();
        buffer = new ByteArrayOutputStream();
        this.textPane = textPane;
    }

    public void append(String data) {
        Document doc = textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), data, null);
            textPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException ex) {
        }
    }

    @Override
    public void flush() throws IOException {
        append(buffer.toString("UTF-8"));
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
