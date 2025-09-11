/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.uusbd;


/**
 * Pipe.
 *
 * @author <a href=mailto:vavivavi@yahoo.co.jp>nsano</a>
 * @version 0.00 030314 nsano initial version <br>
 */
public class Pipe {

    /** */
    private boolean overlap;

    /** */
    private final int interfaceNo;

    /** */
    private final int pipeNo;

    /**
     * Create an object to access the endpoints of a USB device.
     * In the current version, this is valid for interrupt transfer and bulk transfer endpoints.
     *
     * @param interfaceNo Interface Number
     * @param pipeNo Pipe Number
     * @throws UsbException The specified pipe does not exist on the USB device
     */
    public Pipe(Usb usb, int interfaceNo, int pipeNo) throws UsbException {

        this.usbInstance = usb.instance;

        this.interfaceNo = interfaceNo;
        this.pipeNo = pipeNo;

        open(usbInstance, interfaceNo, pipeNo, false);
    }

    /**
     * Creates an object for accessing a USB device endpoint in overlapped mode.
     *
     * @param interfaceNo Interface Number
     * @param pipeNo Pipe Number
     * @param overlapped Overlap
     * @throws UsbException The specified pipe does not exist on the USB device
     */
    public Pipe(Usb usb, int interfaceNo, int pipeNo, boolean overlapped)
        throws UsbException {

        this.usbInstance = usb.instance;

        this.interfaceNo = interfaceNo;
        this.pipeNo = pipeNo;

        open(usbInstance, interfaceNo, pipeNo, overlapped);
    }

    /**
     * Close the pipe.
     */
    public native void close();

    /**
     * TODO
     */
    public native int read() throws UsbException;

    /**
     * Loading.
     */
    public native int read(byte[] b, int off, int len) throws UsbException;

    /**
     * TODO
     */
    public native void write(int b) throws UsbException;

    /**
     * I will write it down.
     */
    public native void write(byte[] b, int off, int len) throws UsbException;

    // ----

    /** USB Handle */
    private final long usbInstance;

    /** Pipe handle */
    private long instance;

    /**
     * Create a pipe.
     *
     * @param usbHandle USB Handle
     * @param interfaceNo Interface Number
     * @param pipeNo Pipe Number
     * @param overlapped Overlap
     * @throws UsbException The specified pipe does not exist on the USB device
     */
    private native void open(long usbHandle,
                             int interfaceNo,
                             int pipeNo,
                             boolean overlapped)
        throws UsbException;

    /**
     * Reset the pipe.
     */
    private native void reset() throws UsbException;

    /**
     * Aborts the pipe.
     */
    private native void abort() throws UsbException;

    /* */
    static {
        System.loadLibrary("UusbdWrapper");
    }
}
