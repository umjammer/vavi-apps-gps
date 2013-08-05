/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.nmea;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * NmeaGpsFormatTest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/03/21 umjammer initial version <br>
 */
public class NmeaGpsFormatTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    /** */
    public static void main(String[] args) throws Exception {
        NmeaGpsFormat ngf = new NmeaGpsFormat();

        // tests checksum
        String[] testTypes = {
            "GGA", "GLL", "VTG", "GGA"
        };

        String[] testData = {
          "050945.00,3504.227794,N,13545.810149,E,1,06,1.4,151.00,M,34.53,M,,",
          "3504.227794,N,13545.810149,E,050945.00,A,A",
          "57.1,T,,,000.0,N,000.0,K,A",
          "123519,4807.038,N,01131.324,E,1,08,0.9,545.4,M,46.9,M,,"
        };

        for (int i = 0; i < testTypes.length; i++) {
            String sentence = ngf.toSentence(testTypes[i], testData[i]);
            System.err.print(sentence);
        }
    }
}

/* */
