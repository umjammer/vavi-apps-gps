/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.nmea;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Properties;
import vavi.gps.BasicGpsDevice;
import vavi.gps.GpsFormat;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * NMEA device.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030320 nsano initial version <br>
 *          0.01 030326 nsano specification compliant <br>
 *          0.02 030326 nsano extends BasicGpsDevice <br>
 */
public class Nmea extends BasicGpsDevice {

    private static final Logger logger = getLogger(Nmea.class.getName());

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
    private final GpsFormat gpsFormat = new NmeaGpsFormat();

    @Override
    protected GpsFormat getGpsFormat() {
        return gpsFormat;
    }

    /** */
    public Nmea(String name) {
        super(name);

        try {
            Properties props = new Properties();

            props.load(Nmea.class.getResourceAsStream("Nmea.properties"));

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
}
