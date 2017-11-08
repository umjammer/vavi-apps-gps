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
import vavi.gps.vendor.sony.IpsGpsFormat;


/**
 * Test1. europe
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/03/21 umjammer initial version <br>
 */
public class Test3 {

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
            System.out.printf("%s, %s\t%s\n",
                              gpsData.getPoint().getLatitude(),
                              gpsData.getPoint().getLongitude(),
                              sdf.format(gpsData.getDateTime()));
        }
        scanner.close();
    }
}

/* */
