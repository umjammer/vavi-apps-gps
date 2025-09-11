/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vavi.util.event.GenericListener;

import static java.lang.System.getLogger;


/**
 * Multicast.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030326 nsano initial version <br>
 *          0.01 030328 nsano fix addGenericListener <br>
 */
public class Multicast extends GpsDevice {

    private static final Logger logger = getLogger(Multicast.class.getName());

    /** */
    private final List<GpsDevice> outputDevices = new ArrayList<>();

    /** */
    public Multicast(String file) {

        try {
            Properties props = new Properties();

            props.load(Multicast.class.getResourceAsStream(file));

            int i = 0;
            while (true) {
                String key = "multicast.outputDevices.class." + i;
                String clazz = props.getProperty(key);
                if (clazz == null) {
logger.log(Level.DEBUG, "device." + i + " not found, break");
                    break;
                }
                key = "multicast.outputDevices.name." + i;
                String name = props.getProperty(key);
                GpsDevice device = BasicGpsDevice.newInstance(clazz, name);
logger.log(Level.DEBUG, "device." + i + ": " + name + ": " + clazz);
                outputDevices.add(device);
                i++;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /** Starts children. */
    @Override
    public void start() {
logger.log(Level.TRACE, "here");
        for (GpsDevice outputDevice : outputDevices) {
            outputDevice.start();
        }
    }

    /** @throws UnsupportedOperationException always be thrown */
    @Override
    protected Runnable getInputThread() {
        throw new UnsupportedOperationException("This class cannot be input device.");
    }

    @Override
    protected GenericListener getOutputGenericListener() {

        return ev -> {

            for (GpsDevice outputDevice : outputDevices) {
                GenericListener listener = outputDevice.getOutputGenericListener();
                listener.eventHappened(ev);
            }
        };
    }

    @Override
    public void addGenericListener(GenericListener listener) {
        for (GpsDevice outputDevice : outputDevices) {
            outputDevice.addGenericListener(listener);
        }
    }
}
