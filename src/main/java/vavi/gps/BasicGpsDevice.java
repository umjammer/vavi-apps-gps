/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import vavi.io.IODevice;
import vavi.io.IODeviceInputStream;
import vavi.io.IODeviceOutputStream;
import vavi.util.Debug;
import vavi.util.event.GenericEvent;
import vavi.util.event.GenericListener;

import static java.lang.System.getLogger;


/**
 * BasicGpsDevice.
 * Subclasses must have a constructor with the signature (Ljava/lang/String;).
 *
 * @see #newInstance(String,String)
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030326 nsano initial version <br>
 *          0.01 030328 nsano be free from SocketException <br>
 */
public abstract class BasicGpsDevice extends GpsDevice {

    private static final Logger logger = getLogger(BasicGpsDevice.class.getName());

    /** */
    public static GpsDevice newInstance(String className, String name)
        throws ClassNotFoundException,
               NoSuchMethodException,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException {

        return (GpsDevice) newInstanceInternal(className, name);
    }

    /** */
    private static Object newInstanceInternal(String className, String name)
        throws ClassNotFoundException,
               NoSuchMethodException,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException {
//logger.log(Level.DEBUG, className + ": " + name);
        Class<?> clazz = Class.forName(className);
        Constructor<?> c = clazz.getConstructor(String.class);
        return c.newInstance(name);
    }

    /** Identifier (specify the one listed in the properties file for each device) */
    protected final String name;

    /**
     * @param name Specify which IODevice to use from the properties file
     */
    public BasicGpsDevice(String name) {
        this.name = name;
    }

    // ----

    /** Gets the formatter for this device. */
    protected abstract GpsFormat getGpsFormat();

    /** Gets the IO device class of this device. */
    protected abstract String getIODeviceClass();

    /**
     * Gets the IO device class identifier for this device.
     * (Serial port name and IP port number are specified to avoid duplicate opening)
     */
    protected abstract String getIODeviceName();

    /** */
    protected IODeviceInputStream is;

    /** */
    protected IODeviceOutputStream os;

    /** Identifier of IO device, pair of IO devices */
    private final Map<String,IODevice> ioDevices = new HashMap<>();

    /**
     * Gets the IO device.
     * The implementation class of {@link IODevice} instantiated here
     * must have a constructor with the signature (Ljava/lang/String;).
     */
    private IODevice getIODevice()
        throws ClassNotFoundException,
               NoSuchMethodException,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException {

        String className = getIODeviceClass();
        String name = getIODeviceName();

        if (ioDevices.containsKey(name)) {
            return ioDevices.get(name);
        } else {
            IODevice ioDevice = (IODevice) newInstanceInternal(className, name);
            ioDevices.put(name, ioDevice);
logger.log(Level.DEBUG, "name: " + name + ": " + className);
            return ioDevice;
        }
    }

    /**
     * If the input stream is not already open, it will be opened.
     * The reason why the constructor does not open is because there are devices that only have one side.
     */
    protected void makeSureInputStreamOpened() {
        if (this.is != null) {
            return;
        }

        try {
            IODevice ioDevice = getIODevice();
            this.is = new IODeviceInputStream(ioDevice);
        } catch (Exception e) {
if (e instanceof InvocationTargetException) {
 Throwable t = ((InvocationTargetException) e).getTargetException();
 logger.log(Level.ERROR, t.getMessage(), t);
} else {
 logger.log(Level.ERROR, e.getMessage(), e);
}
            throw new InternalError(e.toString());
        }
    }

    @Override
    protected Runnable getInputThread() {
        makeSureInputStreamOpened();

        return new Runnable() {
            @Override
            public void run() {
logger.log(Level.DEBUG, "IN[" + getIODeviceName() + "]: thread started");

                while (loop) {
                    byte[] line = null;
                    try {
                        line = is.readLine().getBytes();
                        GpsData gpsData = getGpsFormat().parse(line);
                        fireEventHappened(new GenericEvent(this,
                                                           "data",
                                                           gpsData));
                    } catch (IllegalArgumentException e) {
//logger.log(Level.ERROR, e.getMessage(), e)e);
System.err.println("IN[" + getIODeviceName() + "]> " + new String(line));
                    } catch (IOException e) {
if (e instanceof java.net.SocketException) {
 logger.log(Level.ERROR, e.getMessage());
} else {
 logger.log(Level.ERROR, e.getMessage(), e);
}
                    }
                }

logger.log(Level.DEBUG, "IN[" + getIODeviceName() + "]: thread stoped");
            }
        };
    }

    /**
     * If the output stream is not already open, it will be opened.
     * The reason why the constructor does not open is because there are devices that only have one side.
     */
    protected void makeSureOutputStreamOpened() {
        if (this.os != null) {
            return;
        }

        try {
            IODevice ioDevice = getIODevice();
            this.os = new IODeviceOutputStream(ioDevice);
        } catch (Exception e) {
if (e instanceof InvocationTargetException) {
 Throwable t = ((InvocationTargetException) e).getTargetException();
 logger.log(Level.ERROR, t.getMessage(), t);
} else {
 logger.log(Level.ERROR, e.getMessage(), e);
}
            throw new InternalError(e.toString());
        }
    }

    @Override
    protected GenericListener getOutputGenericListener() {
        makeSureOutputStreamOpened();

        // TODO check multiple instantiation
        return ev -> {
            try {
                GpsData gpsData = (GpsData) ev.getArguments()[0];
                byte[] line = getGpsFormat().format(gpsData);
                os.writeLine(new String(line));
            } catch (Exception e) {
if (e instanceof java.net.SocketException) {
 logger.log(Level.ERROR, e.getMessage());
} else {
 logger.log(Level.ERROR, e.getMessage(), e);
}
            }
        };
    }
}
