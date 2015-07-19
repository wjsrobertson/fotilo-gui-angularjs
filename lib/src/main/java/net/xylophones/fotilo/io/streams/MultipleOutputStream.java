package net.xylophones.fotilo.io.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.google.common.collect.Maps.newHashMap;

/**
 *
 */
public class MultipleOutputStream extends OutputStream {

    private static final ConcurrentSkipListSet<OutputStream> outputStreams = new ConcurrentSkipListSet<>();

    private static final ThreadLocal<Map<OutputStream, IOException>> exceptions = new ThreadLocal<Map<OutputStream, IOException>>() {
        @Override
        protected Map<OutputStream, IOException> initialValue() {
            return newHashMap();
        }
    };

    public void addOutputStream(OutputStream outputStream) {
        outputStreams.add(outputStream);
    }

    public void removeOutputStream(OutputStream outputStream) {
        outputStreams.remove(outputStream);
    }

    @Override
    public void write(int b) throws MultipleOutputStreamIOException {
        boolean exceptionCaught = false;

        for (OutputStream outputStream : outputStreams) {
            try {
                outputStream.write(b);
            } catch (IOException e) {
                exceptionCaught = true;
                storeInThreadLocal(outputStream, e);
            }
        }

        if (exceptionCaught) {
            throwExceptionWithDetails();
        }
    }

    private void storeInThreadLocal(OutputStream outputStream, IOException e) {
        exceptions.get().put(outputStream, e);
    }

    @Override
    public void write(byte[] b) throws MultipleOutputStreamIOException {
        boolean exceptionCaught = false;

        for (OutputStream outputStream : outputStreams) {
            try {
                outputStream.write(b);
            } catch (IOException e) {
                exceptionCaught = true;
                storeInThreadLocal(outputStream, e);
            }
        }

        if (exceptionCaught) {
            throwExceptionWithDetails();
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws MultipleOutputStreamIOException {
        boolean exceptionCaught = false;

        for (OutputStream outputStream : outputStreams) {
            try {
                outputStream.write(b, off, len);
            } catch (IOException e) {
                exceptionCaught = true;
                storeInThreadLocal(outputStream, e);
            }
        }

        if (exceptionCaught) {
            throwExceptionWithDetails();
        }
    }

    @Override
    public void close() throws MultipleOutputStreamIOException {
        boolean exceptionCaught = false;

        for (OutputStream outputStream : outputStreams) {
            try {
                outputStream.close();
            } catch (IOException e) {
                exceptionCaught = true;
                storeInThreadLocal(outputStream, e);
            }
        }

        if (exceptionCaught) {
            throwExceptionWithDetails();
        }
    }

    @Override
    public void flush() throws MultipleOutputStreamIOException {
        boolean exceptionCaught = false;

        for (OutputStream outputStream : outputStreams) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                exceptionCaught = true;
                storeInThreadLocal(outputStream, e);
            }
        }

        if (exceptionCaught) {
            throwExceptionWithDetails();
        }
    }

    private void throwExceptionWithDetails() throws MultipleOutputStreamIOException {
        Map<OutputStream, IOException> currentExceptions = exceptions.get();
        exceptions.remove();
        throw new MultipleOutputStreamIOException(currentExceptions);
    }
}
