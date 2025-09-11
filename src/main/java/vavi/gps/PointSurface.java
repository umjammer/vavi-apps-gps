/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;

import java.text.DecimalFormat;


/**
 * Latitude, Longitude in DMS.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 *          0.01 030318 nsano change seconds' resolution <br>
 *          0.02 030320 nsano fix seconds <br>
 */
public class PointSurface {

    /** */
    public static final int NORTH_LATITUDE = 0;
    /** */
    public static final int SOUTH_LATITUDE = 1;
    /** */
    public static final int EAST_LONGITUDE = 2;
    /** */
    public static final int WEST_LONGITUDE = 3;

    /** */
    private static final String typeStrings = "NSEW";

    /** identifier */
    private int type;

    /** */
    public void setType(int type) {
        this.type = type;
    }

    /** */
    public int getType() {
        return type;
    }

    /** degrees */
    private int degrees;

    /** */
    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    /** */
    public int getDegrees() {
        return degrees;
    }
    /** minutes */
    private int minutes;

    /** */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /** */
    public int getMinutes() {
        return minutes;
    }

    /** seconds */
    private float seconds;

    /** */
    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    /** */
    public float getSeconds() {
        return seconds;
    }

    /** */
    public String toString() {
        return formatter.toString();
    }

    /** */
    private final Formatter formatter = new Formatter2();

    /** */
    private interface Formatter {
        String toString();
    }

    /** */
    class Formatter1 implements Formatter {
        public String toString() {

            StringBuilder sb = new StringBuilder();

            DecimalFormat df2 = new DecimalFormat("00");
            DecimalFormat df3 = new DecimalFormat("000");
            DecimalFormat df2_4 = new DecimalFormat("00.00");

            sb.append(typeStrings.charAt(type));
            sb.append(" ");

            if (type == EAST_LONGITUDE || type == WEST_LONGITUDE) {
                sb.append(df3.format(degrees));
            } else {
                sb.append(df2.format(degrees));
            }
            sb.append("゜");

            sb.append(df2.format(minutes));
            sb.append("'");

            sb.append(df2_4.format(seconds));
            sb.append("\"");

            return sb.toString();
        }
    }

    /** */
    class Formatter2 implements Formatter {
        public String toString() {

            DecimalFormat df2 = new DecimalFormat("00");
            DecimalFormat df2_4 = new DecimalFormat("00.0000");

            String sb = String.valueOf(degrees) +
                    df2.format(minutes) +
                    df2_4.format(seconds);

            return sb;
        }
    }

    /** */
    public float toFloat() {
        return (degrees * 3600 + minutes * 60 + seconds) / 3600f;
    }

    /** */
    public char getTypeString() {
        return typeStrings.charAt(type);
    }
}
