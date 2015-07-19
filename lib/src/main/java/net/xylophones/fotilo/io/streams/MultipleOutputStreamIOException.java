package net.xylophones.fotilo.io.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class MultipleOutputStreamIOException extends IOException {

    private final Map<OutputStream, IOException> underlyingExceptions = new ConcurrentHashMap<>();

    public MultipleOutputStreamIOException(Map<OutputStream, IOException> underlyingExceptions) {
    }

    public Map<OutputStream, IOException> getUnderlyingExceptions() {
        return underlyingExceptions;
    }
}
