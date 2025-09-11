/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import vavi.gps.BasicGpsData;
import vavi.gps.GpsData;
import vavi.gps.GpsFormat;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * RAW GPS Format.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030326 nsano initial version <br>
 */
public class RawGpsFormat implements GpsFormat {

    private static final Logger logger = getLogger(RawGpsFormat.class.getName());

    /**
     * Since only setRawData is set in GpsData,
     * please avoid setting Raw GpsDevice as inputDevice if possible.
     */
    @Override
    public GpsData parse(byte[] line) {
        BasicGpsData data = new BasicGpsData();
        data.setRawData(line);
        return data;
    }

    @Override
    public byte[] format(GpsData data) {
        byte[] line = ((BasicGpsData) data).getRawData();

        // HGR3 -> IPS-5000
        if (line.length == 148 &&
            line[0] == 'S' &&
            line[1] == 'M' &&
            line[2] == '0' &&
            line[3] == '0') {
            byte[] ips = new byte[108];
            System.arraycopy("SONY80".getBytes(), 0, ips, 0, 6);
            System.arraycopy(line, 6, ips, 6, 97);
            System.arraycopy(line, 143, ips, 103, 5);

            return ips;
        } else {
logger.log(Level.DEBUG, "line: " + new String(line));
            throw new IllegalArgumentException(new String(line));
        }
    }
}
