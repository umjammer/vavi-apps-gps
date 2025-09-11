/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.uusbd;

import java.io.IOException;


/**
 * Usb.
 *
 * @author <a href=mailto:vavivavi@yahoo.co.jp>nsano</a>
 * @version 0.00 030314 nsano initial version <br>
 */
public class Usb {

    /**  */
    public Usb() throws IOException {
        open();
    }

    /**  */
    public Usb(int flag,
               int clazz,
               int subClass,
               int vendor,
               int product,
               byte bcdDevice) throws IOException {
        open(flag, clazz, subClass, vendor, product, bcdDevice);
    }

    // ----

    /** USB Handle */
    long instance;

    /**
     * Open the USB device.
     * There must be a USB device connected to the system,
     * using uusbd.sys as the driver, with no other applications having it open.
     * Call close() after use.
     *
     * @throws UsbException
     */
    private native void open() throws UsbException;

    /**  */
    public static final int MASK_NO = 0;
    /**  */
    public static final int MASK_CLASS = 1;
    /**  */
    public static final int MASK_SUBCLASS = 2;
    /**  */
    public static final int MASK_VENDOR = 4;
    /**  */
    public static final int MASK_PRODUCT = 8;
    /**  */
    public static final int MASK_BCDDEVICE = 16;

    /**
     * open() with search criteria added
     * Call close() after use.
     *
     * <li> TODO Not tested
     *
     * @param flag      Specifies the argument to be used in the device search conditions.
     *                  When determining conditions based on multiple items, use OR.
     *                  <pre>
     *                                          MASK_CLASS When  using class code as a search condition
     *                                          MASK_SUBCLASS    When using subclass code as a search condition
     *                                          MASK_VENDOR      When using vendor ID as a search condition
     *                                          MASK_PRODUCT     When using product ID as a search condition
     *                                          MASK_BCDDEVICE   When using the device release number as a search condition
     *                                          MASK_NO          If you do not use any of the above masks
     *                                   </pre>
     * @param clazz     Device Class Code
     * @param subClass  Subclass Code
     * @param vendor    Vendor ID
     * @param product   Product ID
     * @param bcdDevice Device Release Number
     */
    private native void open(int flag,
                             int clazz,
                             int subClass,
                             int vendor,
                             int product,
                             byte bcdDevice) throws UsbException;

    /**
     * Close.
     */
    public native void close();

    /**  */
    public static final int RECIPIENT_DEVICE = 0;
    /**  */
    public static final int RECIPIENT_INTERFACE = 1;
    /**  */
    public static final int RECIPIENT_ENDPOINT = 2;
    /**  */
    public static final int RECIPIENT_OTHER = 3;

    /**
     * Send a class request.
     * For requests that do not involve data transfer, set wLength to 0 and dir_in to false.
     *
     * @param dir_in    Set to true if receiving data from a USB device
     * @param recipient Specify the destination to be specified in D4..D0 of RequestType.
     *                  Specify one of the following:
     *                  <ul>
     *                     <li>RECIPIENT_DEVICE    device
     *                     <li>RECIPIENT_INTERFACE Interface
     *                     <li>RECIPIENT_ENDPOINT  endpoint
     *                     <li>RECIPIENT_OTHER     others
     *                  </ul>
     * @param bRequest  Request number
     * @param wValue    16-bit value determined between the USB device and the device according to the value of bRequest
     * @param wIndex    A 16-bit value determined between the USB device and the device according to the value of bRequest.
     *                  It is mainly used to pass index values and offset values.
     * @param wLength   Transfer length (bytes) when sending and receiving data
     * @param data      Location of transferred data or storage location of received data
     */
    public native void sendClassRequest(boolean dir_in,
                                        int recipient,
                                        int bRequest,
                                        int wValue,
                                        int wIndex,
                                        int wLength,
                                        byte[] data) throws UsbException;

    /**
     * Send a device request.
     *
     * <li> TODO Not tested
     */
    public native void sendVendorRequest(boolean dir_in,
                                         int recipient,
                                         int bRequest,
                                         int wValue,
                                         int wIndex,
                                         int wLength,
                                         byte[] data) throws UsbException;

    /**
     * Check if the USB device is present.
     *
     * @return true The device is present and working properly
     *         false The device was removed
     *         (The device driver has been removed)
     * @throws UsbException is invalid
     */
    private native boolean available() throws UsbException;

    /**
     * Reset the USB device
     */
    private native void reset() throws UsbException;

//    /**
//     * Get the device descriptor of a USB device
//     */
//    private native DeviceDescriptor getDeviceDescriptor() throws UsbException;

//    /**
//     * Gets the configuration descriptor of a USB device.
//     */
//    private native ConfigurationDescriptor getConfigurationDescriptor() throws UsbException;

//    /**
//     * Get QualifierDescriptor
//     */
//    private native DeviceQualifierDescriptor getDeviceQualifierDescriptor() throws UsbException;

//    /**
//     * Gets the OtherSpeedConfigurationDescriptor for the device.
//     * OtherSpeedConfigurationDescriptor has the same format as ConfigurationDescriptor.
//     * Same as getConfigurationDescriptor, except that the returned content is OtherSpeedConfigurationDescriptor.
//     */
//    private native ConfigurationDescriptor getOtherSpeedConfigurationDescriptor() throws UsbException;

    // ----

    /* */
    static {
        System.loadLibrary("UusbdWrapper");
    }
}
