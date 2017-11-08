/*
 * Copyright (c) 2013 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;


/**
 * Util.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2013/06/02 umjammer initial version <br>
 */
public class Util {

    /** */
    public static double[] japan2world(double lat, double lng) {
        double la = lat;
        double ln = lng;
        lat = la - la * 0.00010695 + ln * 0.000017464 + 0.0046017;
        lng = ln - la * 0.000046038 - ln * 0.000083043 + 0.010040;
        return new double[] { lat, lng };
    }

    /** */
    public static double[] world2japan(double lat, double  lng) {
        double la = lat;
        double ln = lng;
        lat = la + la * 0.00010696 - ln * 0.000017467 - 0.0046020;
        lng = ln + la * 0.000046047 + ln * 0.000083049 - 0.010041;
        return new double[] { lat, lng };
    }
}

/* */
