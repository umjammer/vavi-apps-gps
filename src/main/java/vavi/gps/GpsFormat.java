/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;


/**
 * GPS Format.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public interface GpsFormat {

    /**
     * If there is a possibility that raw data will be output,
     * set line to the return value {@link GpsData} using the {@link BasicGpsData#setRawData(byte[])} method.
     */
    GpsData parse(byte[] line);

    /** */
    byte[] format(GpsData gpsData);
}
