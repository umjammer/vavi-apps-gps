/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.Socket;
import java.net.SocketException;

import vavi.net.inet.InetServer;
import vavi.net.inet.SocketHandlerFactory;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * Output data to virtual com port via network.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 *          0.01 030322 nsano repackage <br>
 *          0.02 030328 nsano remove input task <br>
 */
public class InetServerDevice implements IODevice {

    private static final Logger logger = getLogger(InetServerDevice.class.getName());

    /** */
    private int port = 5750;

    /** */
    private Socket socket;

    /** */
    private InputStream is;

    /** */
    private OutputStream os;

    /**
     * @param name Specify the IP port number
     */
    public InetServerDevice(String name) throws IOException {

        this.port = Integer.parseInt(name);

        InetServer server = new InetServer(port);
        server.setSocketHandlerFactory(acceptanceListenerFactory);
        server.start();
    }

    /** */
    private final SocketHandlerFactory acceptanceListenerFactory = new SocketHandlerFactory() {
        private final Runnable socketHandler = () -> {
        };
        /** */
        @Override
        public Runnable getSocketHandler(Socket socket) {
            InetServerDevice.this.socket = socket;
            try {
                is = socket.getInputStream();
                os = socket.getOutputStream();
            } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            }
            return socketHandler;
        }
    };

    @Override
    public int read() throws IOException {
        while (is == null) { // TODO
            Thread.yield();
            try {
                Thread.sleep(20);
            } catch (Exception ignored) {
            }
        }

        try {
            return is.read();
        } catch (SocketException e) {
            is = null;
            os = null;
            throw e;
        }
    }

    @Override
    public int available() throws IOException {
        if (is != null) {
            try {
                return is.available();
            } catch (SocketException e) {
                is = null;
                os = null;
                throw e;
            }
        } else {
            return 0;
        }
    }

    /**
     * Any writes made while the connection is not established will be discarded.
     */
    @Override
    public void write(int b) throws IOException {
        if (os != null) {
            try {
//logger.log(Level.TRACE, "%02x".formatted(b) + ": " + (char) b);
                os.write(b);
            } catch (SocketException e) {
                is = null;
                os = null;
                throw e;
            }
        }
    }

    /**
     * If a connection is not established, the flush is discarded.
     */
    @Override
    public void flush() throws IOException {
        if (os != null) {
            try {
                os.flush();
            } catch (SocketException e) {
                is = null;
                os = null;
                throw e;
            }
        }
    }

    @Override
    public void close() throws IOException {

        try {
            os.close();
            is.close();

            socket.close();
        } finally {
            os = null; // Used as a flag for establishing a connection
            is = null; // Used as a flag for establishing a connection
        }
    }
}
