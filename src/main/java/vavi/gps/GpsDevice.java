/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;

import vavi.util.event.GenericEvent;
import vavi.util.event.GenericListener;
import vavi.util.event.GenericSupport;


/**
 * GPS Device.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030320 nsano initial version <br>
 *          0.01 030326 nsano add comment <br>
 *          0.02 030326 nsano deal multicast <br>
 *          0.03 030328 nsano change TimerTask to Runnable <br>
 */
public abstract class GpsDevice {

    /**
     * Gets the thread that polls the input device.
     * Write the following in the <code>{@link java.lang.Runnable#run()}</code> method:
     * <ol>
     * <li>Read one block from the input device</li>
     * <li>1. Convert the data to <code>{@link vavi.gps.GpsData}</code> or its subclass</li>
     * <li>Create a <code>{@link vavi.util.event.GenericEvent}</code> with argument 2. data and name "data".</li>
     * <li>3. Issue the <code>fireEventHappened()</code> method with the event as an argument</li>
     * </ol>
     * @return    java.lang.Runnable implementation class
     */
    protected abstract Runnable getInputThread();

    /** TODO Multiple connections not supported */
    protected volatile boolean loop;

    /** Make the device work. */
    public void start() {
        loop = true;
        Thread thread = new Thread(getInputThread());
        thread.start();
    }

    /**
     * Gets the event listener that outputs data to the output device.
     * Write the following in the
     * <code>{@link vavi.util.event.GenericListener#eventHappened(GenericEvent)}</code> method.
     * <ol>
     * <li>Get data of type <code>{@link vavi.gps.GpsData}</code> from getArgument() of the event
     * whose getName() of <code>{@link vavi.util.event.GenericEvent}</code> is "name".</li>
     * <li>1. Convert the data into a format appropriate for the output device</li>
     * <li>Output the data from step 2 to the output device.
     * </ol>
     * @return Implementation class of {@link vavi.util.event.GenericListener}
     */
    protected abstract GenericListener getOutputGenericListener();

    /** Connect your input and output devices. */
    public void connect(GpsDevice outputDevice) {
        // out@outputDevice -> in@inputDevice
        GenericListener listener = outputDevice.getOutputGenericListener();
        addGenericListener(listener);
        // out@inputDevice -> in@outputDevice
        listener = getOutputGenericListener();
        outputDevice.addGenericListener(listener);
    }

    // ----

    /** Implementing a general-purpose event mechanism */
    private final GenericSupport gs = new GenericSupport();

    /** Add a generic event listener. */
    public void addGenericListener(GenericListener listener) {
        gs.addGenericListener(listener);
    }

    /** Removes a generic event listener. */
    public void removeGenericListener(GenericListener listener) {
        gs.removeGenericListener(listener);
    }

    /** Publishes a generic event. */
    protected void fireEventHappened(GenericEvent ev) {
        gs.fireEventHappened(ev);
    }
}
