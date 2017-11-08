/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.ms;

import java.io.IOException;
import java.util.Properties;
import vavi.gps.BasicGpsDevice;
import vavi.gps.GpsFormat;
import vavi.util.Debug;


/**
 * AutoRoute device.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 120321 nsano initial version <br>
 */
public class AutoRoute extends BasicGpsDevice {

    /** */
    private String ioDeviceClass = "vavi.gps.io.InetServerDevice";

    /** */
    private String ioDeviceName = "";

    /** */
    protected String getIODeviceClass() {
        return ioDeviceClass;
    }

    /** */
    protected String getIODeviceName() {
        return ioDeviceName;
    }

    /** */
    private GpsFormat gpsFormat = new AutoRouteGpsFormat();

    /** */
    protected GpsFormat getGpsFormat() {
        return gpsFormat;
    }

    /** */
    public AutoRoute(String name) {
        super(name);

        try {
            Properties props = new Properties();

            props.load(AutoRoute.class.getResourceAsStream("Nmea.properties"));

            String key = "ioDevice.class." + this.name;
            String value = props.getProperty(key);
            if (value != null) {
                ioDeviceClass = value;
Debug.println("ioDevice: " + ioDeviceClass);
            }

            key = "ioDevice.name." + this.name;
            value = props.getProperty(key);
            if (value != null) {
                ioDeviceName = value;
Debug.println("name: " + ioDeviceName);
            }
        } catch (IOException e) {
Debug.printStackTrace(e);
            throw new InternalError(e.toString());
        }
    }
}

/* */
