/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.ms;

import vavi.gps.BasicGpsData;
import vavi.gps.GpsData;
import vavi.gps.GpsFormat;


/**
 * AutoRoute 2003
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 120321 nsano initial version <br>
 */
public class AutoRouteGpsFormat implements GpsFormat {

    /** TODO not implemented */
    public GpsData parse(byte[] line) {
        BasicGpsData data = new BasicGpsData();
        data.setRawData(line);
        return data;
    }

    /** without cr,lf */
    public byte[] format(GpsData gpsData) {
        return null;
    }
}

/* */
