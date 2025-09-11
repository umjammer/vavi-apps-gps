/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.gps;

import java.lang.reflect.InvocationTargetException;

import vavi.gps.BasicGpsDevice;
import vavi.gps.GpsDevice;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * GPS Format conveter.
 *
 * <pre>
 * <li>
 * Items to set in GpsFormatConverter.properties
 * </li>
 *
 *  Input Device Class   inputDevice.class
 *  Input Device Name    inputDevice.name
 *  Output Device Class  outputDevice.class
 *  Output Device Name   outputDevice.name
 *
 * <li>
 * If {@link vavi.gps.Multicast} is specified as the output device,
 * set the list of output devices to multicast.
 * </li>
 *
 *  Output Device Class   multicast.outputDevice.class.#
 *  Output Device Name    multicast.outputDevice.name.#
 *  (# is a sequential number starting from 0)
 * </pre>
 *
 * @depends GpsFormatConverter.properties
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
@PropsEntity(url = "classpath:GpsFormatConverter.properties")
public class GpsFormatConverter {

    @Property(name = "inputDevice.class")
    private String inputClass = "vavi.gps.vendor.sony.Hgr";

    @Property(name = "inputDevice.name")
    private String inputName = "HGR3S";

    @Property(name = "outputDevice.class")
    private String outputClass = "vavi.gps.vendor.nmea.Nmea";

    @Property(name = "outputDevice.name")
    private String outputName = "COM3";

    /** */
    private final GpsDevice inputDevice;

    /** */
    private final GpsDevice outputDevice;

    /** */
    public GpsFormatConverter() {

        try {
            PropsEntity.Util.bind(this);

            inputDevice = BasicGpsDevice.newInstance(inputClass, inputName);

            outputDevice = BasicGpsDevice.newInstance(outputClass, outputName);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException)
                Debug.printStackTrace(((InvocationTargetException) e).getTargetException());
            else
                Debug.printStackTrace(e);
            throw new InternalError(e.toString());
        }

        // start
        inputDevice.connect(outputDevice);
        inputDevice.start();
        outputDevice.start();
    }

    // ----

    /** */
    public static void main(String[] args) {
        try {
            new GpsFormatConverter();
        } catch (Throwable e) {
            Debug.printStackTrace(e);
            System.exit(1);
        }
    }
}
