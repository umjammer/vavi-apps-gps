/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.gps;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import vavi.io.IODevice;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * HgrDevice
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030327 nsano initial version <br>
 *          0.01 030331 nsano be generic <br>
 */
public class HgrDevice implements IODevice {

    private static final Logger logger = getLogger(HgrDevice.class.getName());

    /** */
    private HgrDevice() throws IOException {
    }

    /** */
    private static IODevice device;

    /** */
    public static IODevice getInstance() {
        try {
            if (device == null) {
//                device = new vavi.gps.vendor.test.DummyHgrDevice("HGR3S");
                device = new vavi.gps.vendor.sony.HgrUsbDevice("HGR3S");
            }
            return device;
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new InternalError(e.getMessage());
        }
    }

    @Override
    public int read() throws IOException {
        return device.read();
    }

    @Override
    public int available() throws IOException {
        return device.available();
    }

    @Override
    public void write(int c) throws IOException {
        device.write(c);
    }

    @Override
    public void flush() throws IOException {
        device.flush();
    }

    @Override
    public void close() throws IOException {
        device.close();
    }
}
