/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;


/**
 * Channel.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class Channel {
    /** Satellite PRN number 1 - 32 */
    private int prn;

    /** */
    public void setPrn(int prn) {
        this.prn = prn;
    }

    /** */
    public int getPrn() {
        return prn;
    }

    /** */
    public static final int INVALID_ELEVTION = 99;

    /** Satellite altitude */
    private int elevation;

    /** */
    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    /** */
    public int getElevation() {
        return elevation;
    }

    /** */
    public static final int INVALID_AZIMUTH = 999;

    /** Satellite orientation */
    private int azimuth;

    /** */
    public void setAzimuth(int azimuth) {
        this.azimuth = azimuth;
    }

    /** */
    public int getAzimuth() {
        return azimuth;
    }

    /** Receiver and satellite information 0 - 5 */
    private int info;

    /** */
    public void setInfo(int info) {
        this.info = info;
    }

    /** */
    public int getInfo() {
        return info;
    }

    /** Satellite signal strength 0 - 25 */
    private int signalStrength;

    /** */
    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    /** */
    public int getSignalStrength() {
        return signalStrength;
    }

    public static final int INFO_SCAN = 0;
    public static final int INFO_LOCK = 1;
    public static final int INFO_READY = 2;
    public static final int INFO_HOLD = 3;
    public static final int INFO_ILL = 4;
    public static final int INFO_OK = 5;

    // ----

    /** */
    public boolean available() {
        return elevation != INVALID_ELEVTION && azimuth != INVALID_AZIMUTH;
    }

    /** */
    public String getKeyString() {
        return "PRN" + prn;
    }

    /** */
    public String toString() {
        return "PRN" + prn + ": " +
            "E=" + elevation + ", " +
            "A=" + azimuth + ", " +
            "I=" + infoStrings[info] + ", " +
            "S=" + signalStrength;
    }

    /** Receiver and satellite information */
    private static final String[] infoStrings = {
        "SCAN", "LOCK", "READY", "HOLD", "ILL", "OK"
    };
}
