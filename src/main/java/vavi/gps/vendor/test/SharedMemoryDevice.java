/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.IOException;
import java.util.Queue;

import vavi.io.IODevice;


/**
 * SharedMemoryDevice.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030331 nsano initial version <br>
 */
public class SharedMemoryDevice implements IODevice {

    /** */
    private final Queue<Integer> inputFifo;

    /** */
    private final Queue<Integer> outputFifo;

    /** */
    public SharedMemoryDevice(String reader, String writer) {
        inputFifo = SharedFifo.newInstance(reader);
        outputFifo = SharedFifo.newInstance(writer);
    }

    @Override
    public int read() throws IOException {
        while (inputFifo.peek() == null) {    // TODO
            Thread.yield();
            try { Thread.sleep(20); } catch (Exception ignored) {}
//Thread.currentThread().getThreadGroup().list();
        }

        int c = inputFifo.poll();
//System.err.println(StringUtil.toHex2(c));
        return c;
    }

    @Override
    public int available() throws IOException {
        return inputFifo.size();
    }

    @Override
    public void write(int b) throws IOException {
//logger.log(Level.DEBUG, String.valueOf((char) b));
        outputFifo.offer(b);
    }

    @Override
    public void flush() throws IOException {
//logger.log(Level.DEBUG, "<< " + outputFifo.size() + ", >> " + inputFifo.size());
    }

    @Override
    public void close() throws IOException {
    }
}
