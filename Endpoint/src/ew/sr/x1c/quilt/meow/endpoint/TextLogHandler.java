package ew.sr.x1c.quilt.meow.endpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class TextLogHandler extends StreamHandler {

    private void configure() {
        setFormatter(new SimpleFormatter());
        try {
            setEncoding("UTF-8");
        } catch (IOException ex) {
            try {
                setEncoding(null);
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    public TextLogHandler(OutputStream os) {
        super();
        configure();
        setOutputStream(os);
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    @Override
    public void close() {
        flush();
    }
}
