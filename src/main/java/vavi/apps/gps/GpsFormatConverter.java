/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.gps;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import vavi.gps.BasicGpsDevice;
import vavi.gps.GpsDevice;
import vavi.util.Debug;


/**
 * GPS Format conveter.
 * 
 * <pre>
 * <li>
 * GpsFormatConverter.properties �ɐݒ肷�鍀��
 * </li>
 * 
 *  ���̓f�o�C�X�N���X	inputDevice.class
 *  ���̓f�o�C�X��	inputDevice.name
 *  �o�̓f�o�C�X�N���X	outputDevice.class
 *  �o�̓f�o�C�X��	outputDevice.name
 * 
 * <li>
 * �o�̓f�o�C�X�Ƃ��� {@link vavi.gps.Multicast} ���w�肵���ꍇ
 *  �}���`�L���X�g����o�̓f�o�C�X�̃��X�g��ݒ肷��
 * </li>
 * 
 *  �o�̓f�o�C�X�N���X	multicast.outputDevice.class.#
 *  �o�̓f�o�C�X��	multicast.outputDevice.name.#
 *  (# �� 0 ����A��)
 * </pre>
 * 
 * @depends GpsFormatConverter.properties
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class GpsFormatConverter {

    /** */
    private String inputClass = "vavi.gps.vendor.sony.Hgr";

    /** */
    private String inputName = "HGR3S";

    /** */
    private String outputClass = "vavi.gps.vendor.nmea.Nmea";

    /** */
    private String outputName = "COM3";

    /** */
    private GpsDevice inputDevice;

    /** */
    private GpsDevice outputDevice;

    /** */
    public GpsFormatConverter() {

        final String filename = "GpsFormatConverter.properties";

        try {
            Properties props = new Properties();

            props.load(GpsFormatConverter.class.getResourceAsStream(filename));

            String value = props.getProperty("inputDevice.class");
            if (value != null) {
                inputClass = value;
                Debug.println("input: " + inputClass);
            }
            value = props.getProperty("inputDevice.name");
            if (value != null) {
                inputName = value;
                Debug.println("input: " + inputName);
            }

            inputDevice = BasicGpsDevice.newInstance(inputClass, inputName);

            value = props.getProperty("outputDevice.class");
            if (value != null) {
                outputClass = value;
                Debug.println("output: " + outputClass);
            }
            value = props.getProperty("outputDevice.name");
            if (value != null) {
                outputName = value;
                Debug.println("output: " + outputName);
            }

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

    // -------------------------------------------------------------------------

    /** */
    public static void main(String args[]) {
        try {
            new GpsFormatConverter();
        } catch (Throwable e) {
            Debug.printStackTrace(e);
            System.exit(1);
        }
    }
}

/* */
