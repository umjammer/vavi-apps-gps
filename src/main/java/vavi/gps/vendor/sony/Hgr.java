/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.sony;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Properties;

import vavi.gps.BasicGpsDevice;
import vavi.gps.GpsFormat;
import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * HGR device.
 *
 * Session with Navin' You 5.5
 * <code><pre>
 * < !PUON\r\n        0, 9, 0x100
 * > ROM    OK\r\n
 * > RS232C OK\r\n
 * > CLOCK  NG\r\n
 * > \r\n
 * >         ----< SONY GLOBAL POSITIONING SYSTEM >-----\r\n
 * > \r\n
 * >                                (C)Copyright 1991,1997   Sony Corporation.\r\n
 * > \r\n
 * < !PC\r\n
 * > OK\r\n
 * < !ID\r\n
 * > IDDTPCQ-HGR3,1.0.00.07281\r\n
 * < !GP
 * > OK\r\n
 * < @VF040\r\n
 * > \r\n
 * > @VF040\r\n
 * > \r\n
 * < @SKB\r\n
 * > \r\n
 * > @SKB\r\n
 * > \r\n
 * :
 * < !PUOFF\r\n
 * </pre></code>
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 *          0.01 030318 nsano use property file <br>
 *          0.02 030320 nsano extends GpsDevice <br>
 *          0.03 030324 nsano add logging <br>
 *          0.04 030326 nsano extends BasicGpsDevice <br>
 *          0.05 030327 nsano separate logging <br>
 *          0.06 030424 nsano add @VF command <br>
 */
public class Hgr extends BasicGpsDevice {

    private static final Logger logger = getLogger(Hgr.class.getName());

    /** Power on command */
    public static final String INTERFACE_POWER_ON = "!PUON";
    /** Power off command */
    public static final String INTERFACE_POWER_OFF = "!PUOFF";
    /** Command to put HGR into command sending mode */
    public static final String INTERFACE_MODE_PC = "!PC";
    /** Command to put HGR into positioning mode */
    public static final String INTERFACE_MODE_GP = "!GP";
    /** Command to get ID */
    public static final String INTERFACE_ID_GET = "!ID";
    /** */
    public static final String INTERFACE_MEMORY_WRITE = "!MW";
    /** */
    public static final String INTERFACE_MEMORY_READ = "!MR";
    /** */
    public static final String INTERFACE_MEMORY_DUMP = "!MD";

    /** */
    public static final String COMMAND_UNKNOWN1 = "@VF";
    /** Commands to change the datum */
    public static final String COMMAND_MAP_DATUM = "@SK";

    //-------------------------------------------------------------------------

    /** */
    private String ioDeviceClass = "vavi.gps.vendor.sony.HgrUsbDevice";

    /** */
    private String ioDeviceName = "HGR3S";

    @Override
    protected String getIODeviceClass() {
        return ioDeviceClass;
    }

    @Override
    protected String getIODeviceName() {
        return ioDeviceName;
    }

    /** */
    private final GpsFormat gpsFormat = new IpsGpsFormat();

    @Override
    protected GpsFormat getGpsFormat() {
        return gpsFormat;
    }

    /** */
    private String mapDatum = "B"; // Default is Tokyo
    /** */
    private String vfValue = "040";

    /** */
    public Hgr(String name) {
        super(name);

        try {
            Properties props = new Properties();

            props.load(Hgr.class.getResourceAsStream("Hgr.properties"));

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

            value = props.getProperty("hgr.mapDatum");
            if (value != null) {
                mapDatum = value;
logger.log(Level.DEBUG, "mapDatum: " + mapDatum);
            }

            value = props.getProperty("hgr.vfValue");
            if (value != null) {
                vfValue = value;
logger.log(Level.DEBUG, "vfValue: " + vfValue);
            }
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new InternalError(e.toString());
        }
    }

    @Override
    public void start() {

        super.start();

        try {
            makeSureOutputStreamOpened();

            os.writeLine(INTERFACE_POWER_ON);

            os.writeLine(INTERFACE_MODE_PC);

            os.writeLine(INTERFACE_ID_GET);

            os.writeLine(INTERFACE_MODE_GP);

            os.writeLine(COMMAND_UNKNOWN1 + vfValue);

            os.writeLine(COMMAND_UNKNOWN1 + vfValue);

            os.writeLine(COMMAND_MAP_DATUM + mapDatum);
            // It won't be reflected unless you do it twice.
            os.writeLine(COMMAND_MAP_DATUM + mapDatum);
try { Thread.sleep(3000); } catch (Exception e) { logger.log(Level.ERROR, e.getMessage(), e); }
            os.writeLine(COMMAND_UNKNOWN1 + vfValue);

            os.writeLine(COMMAND_UNKNOWN1 + vfValue);

            os.writeLine(COMMAND_MAP_DATUM + mapDatum);

            os.writeLine(COMMAND_MAP_DATUM + mapDatum);
        } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new InternalError(e.toString());
        }
    }
}
