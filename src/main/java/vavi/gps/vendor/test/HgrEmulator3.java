/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.FileSystems;
import java.util.Properties;
import java.util.TimerTask;

import static java.lang.System.getLogger;


/**
 * This class emulates HGR.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030410 nsano initial version <br>
 */
public class HgrEmulator3 extends HgrEmulator {

    private static final Logger logger = getLogger(HgrEmulator3.class.getName());

    /** Double fast forward speed */
    int ff = 10;

    @Override
    protected TimerTask getOutputTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < ff - 1; i++) {
                        reader.readLine();
                    }
                    String line = reader.readLine();
                    os.writeLine(line);
                }
                catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        };
    }

    // ----

    /** */
    private final String ioDeviceName = "dummy3";

    /** */
    protected String getIODeviceName() {
        return ioDeviceName;
    }

    /** */
    private static String file =
        System.getProperty("user.home") +
                FileSystems.getDefault().getSeparator() +
        ".hgr.log";

    /** */
    private final BufferedReader reader;

    /** */
    public HgrEmulator3() {

        final Class<?> c = HgrEmulator3.class;

        try {
            Properties props = new Properties();

            props.load(c.getResourceAsStream("DummyHgrDevice.properties"));

            String value = props.getProperty("emulator3.ff");
            if (value != null) {
logger.log(Level.DEBUG, "ff: " + value);
                ff = Integer.parseInt(value);
            }

            value = props.getProperty("emulator3.file");
            if (value != null) {
logger.log(Level.DEBUG, "file: " + value);
                file = value;
            }

            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
