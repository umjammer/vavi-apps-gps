/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.sony;

import java.lang.System.Logger.Level;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import vavi.gps.BasicGpsData;
import vavi.gps.GpsDevice;
import vavi.gps.GpsData;
import vavi.util.event.GenericListener;

import static java.lang.System.getLogger;


/**
 * HGR Logger device.
 *
 * TODO Generalized, independent from HGR, absorb differences with Formatter
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030327 nsano initial version <br>
 */
public class HgrLogger extends GpsDevice {

    private static final System.Logger logger = getLogger(HgrLogger.class.getName());

    /** Whether to log */
    private boolean logging = false;

    /** How often to log */
    private int interval = 10;

    /** Logger */
    private Logger log;

    /** What to use TODO name for? Formatter class? */
    public HgrLogger(String name) {

        try {
            Properties props = new Properties();

            props.load(Hgr.class.getResourceAsStream("Hgr.properties"));

            String value = props.getProperty("hgr.logging");
            if (value != null) {
logger.log(Level.DEBUG, "logging: " + value);
                logging = Boolean.parseBoolean(value);
            }

            if (logging) {
                value = props.getProperty("hgr.logging.formatter");
                if (value != null) {
logger.log(Level.DEBUG, "configClass: " + value);
                    Handler h = new FileHandler("%h/.hgr.log", true);
                    Formatter f =
                        (Formatter) Class.forName(value).getDeclaredConstructor().newInstance();

                    h.setFormatter(f);

                    log = Logger.getLogger(Hgr.class.getName());
                    log.setUseParentHandlers(false);
                    log.addHandler(h);
                }

                value = props.getProperty("hgr.logging.interval");
                if (value != null) {
logger.log(Level.DEBUG, "interval: " + value);
            interval = Integer.parseInt(value);
                }
            }
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new InternalError(e.toString());
        }
    }

    /** Does nothing. */
    @Override
    public void start() {
logger.log(Level.DEBUG, "here");
    }

    /** Current Counter */
    private int logCount = 0;

    /** @throws IllegalStateException always be thrown */
    @Override
    protected Runnable getInputThread() {
        throw new IllegalStateException("This class cannot be input device.");
    }

    @Override
    protected GenericListener getOutputGenericListener() {

        return ev -> {
            try {
                GpsData gpsData = (GpsData) ev.getArguments()[0];
//System.err.println((logCount + 1) + "/" + interval);
                if (logging && (logCount == (interval - 1))) {
                    if (gpsData.ready()) {
                        byte[] line = ((BasicGpsData) gpsData).getRawData();
                        logger.log(Level.INFO, new String(line));
                    }
                    logCount = 0;
                } else {
                    logCount++;
                }
            } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
            }
        };
    }
}
