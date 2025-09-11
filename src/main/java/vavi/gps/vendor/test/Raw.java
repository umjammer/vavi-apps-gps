/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Properties;
import vavi.gps.BasicGpsDevice;
import vavi.gps.GpsFormat;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * Raw GPS device.
 * Please avoid setting it as an inputDevice if possible.
 *
 * TODO Generalized and promoted to vavi.gps package
 * @see RawGpsFormat#parse(byte[])
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030325 nsano initial version <br>
 *          0.01 030326 nsano extends BasicGpsDevice <br>
 */
public class Raw extends BasicGpsDevice {

    private static final Logger logger = getLogger(Raw.class.getName());

    /** */
    private String ioDeviceClass = "vavi.gps.io.InetServerDevice";

    /** */
    private String ioDeviceName = "5750";

    @Override
    protected String getIODeviceClass() {
        return ioDeviceClass;
    }

    @Override
    protected String getIODeviceName() {
        return ioDeviceName;
    }

    /** */
    private final GpsFormat gpsFormat = new RawGpsFormat();

    @Override
    protected GpsFormat getGpsFormat() {
        // logger.log(Level.DEBUG, "gpsFormat: " + gpsFormat);
        return gpsFormat;
    }

    /** */
    public Raw(String name) {
        super(name);

        try {
            Properties props = new Properties();

            props.load(Raw.class.getResourceAsStream("Raw.properties"));

            String key = "ioDevice.class." + this.name;
            String value = props.getProperty(key);
            if (value != null) {
                ioDeviceClass = value;
                logger.log(Level.DEBUG, "ioDevice: " + ioDeviceClass);
            }

            key = "ioDevice.name." + this.name;
            value = props.getProperty(key);
            if (value != null) {
                ioDeviceName = value;
                logger.log(Level.DEBUG, "name: " + ioDeviceName);
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new InternalError(e.toString());
        }
    }

    /** Does nothing. */
    @Override
    public void start() {
        logger.log(Level.DEBUG, "here");
    }
}
