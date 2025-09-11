/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.TimerTask;

import static java.lang.System.getLogger;


/**
 * This class emulates HGR.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030331 nsano initial version <br>
 */
public class HgrEmulator1 extends HgrEmulator {

    private static final Logger logger = getLogger(HgrEmulator1.class.getName());

    @Override
    protected TimerTask getOutputTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    os.writeLine(dummyString);
                } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        };
    }

    // ----

    /** */
    private final String ioDeviceName = "dummy1";

    /** */
    protected String getIODeviceName() {
        return ioDeviceName;
    }

    /** */
    private String dummyString;

    /** */
    private static final String file = "tmp/ips.txt";

    /** */
    public HgrEmulator1() {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {

            dummyString = r.readLine();
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
        }
    }
}
