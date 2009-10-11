/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;

import java.io.IOException;
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


/**
 * BasicGpsDevice. 
 * �T�u�N���X�͕K�� (Ljava/lang/String;) �̃V�O�l�`��������
 * �R���X�g���N�^�� �����Ȃ���΂Ȃ�܂���B
 * 
 * @see #newInstance(String,String)
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030326 nsano initial version <br>
 *          0.01 030328 nsano be free from SocketException <br>
 */
public abstract class BasicGpsDevice extends GpsDevice {

    /** */
    public static final GpsDevice newInstance(String className, String name)
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
//Debug.println(className + ": " + name);
        Class<?> clazz = Class.forName(className);
        Constructor<?> c = clazz.getConstructor(String.class);
        return c.newInstance(name);
    }

    /** ���ʎq(�e�f�o�C�X�̃v���p�e�B�t�@�C���Ƀ��X�g���ꂽ���̂��w��) */
    protected String name;

    /**
     * @param name �v���p�e�B�t�@�C���̂ǂ� IODevice ���g�p���邩���w��
     */
    public BasicGpsDevice(String name) {
        this.name = name;
    }

    //-------------------------------------------------------------------------

    /** ���̃f�o�C�X�̃t�H�[�}�b�^���擾���܂��B */
    protected abstract GpsFormat getGpsFormat();

    /** ���̃f�o�C�X�� IO �f�o�C�X�N���X���擾���܂��B */
    protected abstract String getIODeviceClass();

    /**
     * ���̃f�o�C�X�� IO �f�o�C�X�N���X�̎��ʎq���擾���܂��B
     * (�V���A���|�[�g���� IP �̃|�[�g�ԍ����w�肳��d���I�[�v��������܂�)
     */
    protected abstract String getIODeviceName();

    /** */
    protected IODeviceInputStream is;

    /** */
    protected IODeviceOutputStream os;

    /** IO �f�o�C�X�̎��ʎq�AIO �f�o�C�X�̃y�A */
    private Map<String,IODevice> ioDevices = new HashMap<String,IODevice>();

    /**
     * IO �f�o�C�X���擾���܂��B
     * �����ŃC���X�^���X������� {@link IODevice} �̎����N���X�͕K��
     * (Ljava/lang/String;) �̃V�O�l�`�������R���X�g���N�^��
     * �����Ȃ���΂Ȃ�܂���B
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
Debug.println("name: " + name + ": " + className);
            return ioDevice;
        }
    }

    /**
     * ���̓X�g���[�����I�[�v�����Ă��Ȃ���΃I�[�v�����܂��B
     * �R���X�g���N�^�ŃI�[�v�����Ȃ��͕̂Е��݂̂̃f�o�C�X���݂邽��
     */
    protected void makeSureInputStreamOpened() {
        if (this.is != null) {
            return;
        }

        try {
            IODevice ioDevice = getIODevice();
            this.is = new IODeviceInputStream(ioDevice);
        } catch (Exception e) {
if (e instanceof InvocationTargetException)
 Debug.printStackTrace(((InvocationTargetException) e).getTargetException());
else           
 Debug.printStackTrace(e);
            throw new InternalError(e.toString());
        }
    }

    /** */
    protected Runnable getInputThread() {
        makeSureInputStreamOpened();

        return new Runnable() {
            public void run() {
Debug.println("IN[" + getIODeviceName() + "]: thread started");

                while (loop) {
                    byte[] line = null;
                    try {
                        line = is.readLine().getBytes();
                        GpsData gpsData = getGpsFormat().parse(line);
                        fireEventHappened(new GenericEvent(this,
                                                           "data",
                                                           gpsData));
                    } catch (IllegalArgumentException e) {
//Debug.printStackTrace(e);
System.err.println("IN[" + getIODeviceName() + "]> " + new String(line));
                    } catch (IOException e) {
if (e instanceof java.net.SocketException)
 Debug.println(e.getMessage());
else
 Debug.printStackTrace(e);
                    }
                }

Debug.println("IN[" + getIODeviceName() + "]: thread stoped");
            }
        };
    }

    /**
     * �o�̓X�g���[�����I�[�v�����Ă��Ȃ���΃I�[�v�����܂��B
     * �R���X�g���N�^�ŃI�[�v�����Ȃ��͕̂Е��݂̂̃f�o�C�X���݂邽��
     */
    protected void makeSureOutputStreamOpened() {
        if (this.os != null) {
            return;
        }

        try {
            IODevice ioDevice = getIODevice();
            this.os = new IODeviceOutputStream(ioDevice);
        } catch (Exception e) {
if (e instanceof InvocationTargetException)
 Debug.printStackTrace(((InvocationTargetException) e).getTargetException());
else           
 Debug.printStackTrace(e);
            throw new InternalError(e.toString());
        }
    }

    /** */
    protected GenericListener getOutputGenericListener() {
        makeSureOutputStreamOpened();

        // TODO check multiple instantiation
        return new GenericListener() {
            public void eventHappened(GenericEvent ev) {
                try {
                    GpsData gpsData = (GpsData) ev.getArguments()[0];
                    byte[] line = getGpsFormat().format(gpsData);
                    os.writeLine(new String(line));
                } catch (Exception e) {
if (e instanceof java.net.SocketException)
 Debug.println(e.getMessage());
else
 Debug.printStackTrace(e);
                }
            }
        };
    }
}

/* */
