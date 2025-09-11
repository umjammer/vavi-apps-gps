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
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * SerialPortDevice
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030322 nsano initial version <br>
 */
public class SerialPortDevice implements IODevice {

    private static final Logger logger = getLogger(SerialPortDevice.class.getName());

    /** */
    private String serialName = "COM1";

    /** */
    private final SerialPort serialPort;

    /** */
    private final InputStream is;

    /** */
    private final OutputStream os;

    /**
     * TODO read from file, COM port properties (4800, 8, 1, none, ...)
     *
     * @param name Specifies the serial port number
     */
    public SerialPortDevice(String name) {

        this.serialName = name;

        Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
        while (e.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) e.nextElement();
            logger.log(Level.DEBUG, "type: " + portId.getPortType());
            logger.log(Level.DEBUG, "name: [" + portId.getName() + "]");
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals(serialName)) {
                try {
                    serialPort = (SerialPort) portId.open(getClass().getName(), 2000);
                    serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    is = serialPort.getInputStream();
                    os = serialPort.getOutputStream();
                    return;
                } catch (Exception f) {
                    throw new InternalError(f.toString());
                }
            }
        }

        throw new InternalError("no such port: " + serialName);
    }

    // ----

    @Override
    public int read() throws IOException {
        return is.read();
    }

    @Override
    public int available() throws IOException {
        return is.available();
    }

    @Override
    public void write(int b) throws IOException {
        os.write(b);
    }

    @Override
    public void flush() throws IOException {
        os.flush();
    }

    @Override
    public void close() throws IOException {
        is.close();
        os.close();
        serialPort.close();
    }
}
