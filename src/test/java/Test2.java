/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import vavi.gps.PointSurface;
import vavi.gps.Util;


/**
 * Test2. Convert Tokyo Datum and WGS84
 *
 * <pre>
 * nyp -> csv
 * $ nyp2txt
 * csv -> tsv
 * $ c2t
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/03/21 umjammer initial version <br>
 */
public class Test2 {

    /**
     * @in MyPackage.tsv
     * @out MyPackage2.tsv
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        String h = scanner.nextLine();
        System.out.println(h);
        while (scanner.hasNextLine()) {
            String[] lines = scanner.nextLine().split("\t");

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
            Date date = sdf.parse(lines[0]);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            sdf2.setTimeZone(TimeZone.getTimeZone("GMT+9"));

            String[] latlons = lines[1].split(",");
            PointSurface lat = new PointSurface();
            String[] lats = latlons[0].trim().split("\\.");
            lat.setDegrees(Integer.parseInt(lats[0]));
            lat.setMinutes(Integer.parseInt(lats[1].substring(0, 2)));
            lat.setSeconds(Float.parseFloat(lats[1].substring(2, 4) + "." + lats[1].substring(4, 9)));
            PointSurface lng = new PointSurface();
            String[] lngs = latlons[1].trim().split("\\.");
            lng.setDegrees(Integer.parseInt(lngs[0]));
            lng.setMinutes(Integer.parseInt(lngs[1].substring(0, 2)));
            lng.setSeconds(Float.parseFloat(lngs[1].substring(2, 4) + "." + lngs[1].substring(4, 9)));
//System.err.println(lines[1] + " -> " + lat + ", " + lng);
            double[] wgs84 = Util.japan2world(lat.toFloat(), lng.toFloat());
            System.out.printf("%s\t%.9f, %.9f\n", sdf2.format(date), wgs84[0], wgs84[1]);
        }
        scanner.close();
    }
}

/* */
