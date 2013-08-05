/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.FileInputStream;
import java.util.Scanner;

import vavi.gps.PointSurface;


/**
 * Test4.
 * 
 * <pre>
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/03/21 umjammer initial version <br>
 */
public class Test4 {

    /**
     * @in test333.tsv
     * @out 
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        while (scanner.hasNextLine()) {
            String[] lines = scanner.nextLine().split("\t");

            String[] latlons = lines[0].split(",");
            PointSurface lat = new PointSurface();
//System.err.println(latlons[0]);
            String[] lats = latlons[0].trim().split("\\.");
            int s = lats[0].length() - 4;
            lat.setDegrees(Integer.parseInt(lats[0].substring(0, s)));
            lat.setMinutes(Integer.parseInt(lats[0].substring(s, s + 2)));
            lat.setSeconds(Float.parseFloat(lats[0].substring(s + 2, s + 4) + "." + lats[1]));
            PointSurface lng = new PointSurface();
            String[] lngs = latlons[1].trim().split("\\.");
            s = lngs[0].length() - 4;
//System.err.println(lngs[0]);
            lng.setDegrees(Integer.parseInt(lngs[0].substring(0, s)));
            lng.setMinutes(Integer.parseInt(lngs[0].substring(s, s + 2)));
            lng.setSeconds(Float.parseFloat(lngs[0].substring(s + 2, s + 4) + "." + lngs[1]));
//System.err.println(lines[1] + " -> " + lat + ", " + lng);
            System.out.printf("%s\t%.9f, %.9f\n", lines[1], lat.toFloat(), lng.toFloat());
        }
    }
}

/* */
