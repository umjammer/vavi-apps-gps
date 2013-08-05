/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.TimeZone;

import vavi.gps.GpsData;
import vavi.gps.GpsFormat;
import vavi.gps.Util;
import vavi.gps.vendor.sony.IpsGpsFormat;


/**
 * Test1. europe
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/03/21 umjammer initial version <br>
 */
public class Test1 {

    /**
     * 「度のみを用いた場合、小数点以下3桁まで与えれば、度・分・秒によって表すのとほぼ同じ精度が得られる。」
     * 
     * @param args hgr_log_file
     * @see "http://ja.wikipedia.org/wiki/%E5%88%86_(%E8%A7%92%E5%BA%A6)"
     */
    public static void main(String[] args) throws Exception {
        GpsFormat gpsFormat = new IpsGpsFormat();

        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            GpsData gpsData = gpsFormat.parse(line.getBytes());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            double[] wgs84 = Util.japan2world(gpsData.getPoint().getLatitude().toFloat(), gpsData.getPoint().getLongitude().toFloat());
            System.out.printf("%1$s\t%2$7.5f, %3$7.5f\n", sdf.format(gpsData.getDateTime()), wgs84[0], wgs84[1]);
        }
    }
}

/* */
