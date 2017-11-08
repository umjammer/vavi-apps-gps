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
    /** 衛星の PRN 番号 1 - 32 */
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

    /** 衛星の高度 */
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

    /** 衛星の方位 */
    private int azimuth;

    /** */
    public void setAzimuth(int azimuth) {
        this.azimuth = azimuth;
    }

    /** */
    public int getAzimuth() {
        return azimuth;
    }

    /** 受信機と衛星の情報 0 - 5 */
    private int info;

    /** */
    public void setInfo(int info) {
        this.info = info;
    }

    /** */
    public int getInfo() {
        return info;
    }

    /** 衛星からの信号の強さ 0 - 25 */
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

    //----

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

    /** 受信機と衛星の情報 */
    private static final String[] infoStrings = {
        "SCAN", "LOCK", "READY", "HOLD", "ILL", "OK"
    };
}

/* */
