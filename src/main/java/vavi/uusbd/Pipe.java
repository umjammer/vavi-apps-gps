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
    @SuppressWarnings("unused")
    private boolean overlap;

    /** */
    @SuppressWarnings("unused")
    private int interfaceNo;

    /** */
    @SuppressWarnings("unused")
    private int pipeNo;

    /**
     * USB �f�o�C�X�̃G���h�|�C���g�ɃA�N�Z�X���邽�߂̃I�u�W�F�N�g���쐬����B
     * ���݂̃o�[�W�����ł̓C���^�[���v�g�]���A
     * �o���N�]���̃G���h�|�C���g�ɂ��ėL���ł���B
     * 
     * @param interfaceNo �C���^�[�t�F�[�X�ԍ�
     * @param pipeNo �p�C�v�ԍ�
     * @throws UsbException USB�f�o�C�X�Ɏw�肵���p�C�v������
     */
    public Pipe(Usb usb, int interfaceNo, int pipeNo) throws UsbException {

        this.usbInstance = usb.instance;

        this.interfaceNo = interfaceNo;
        this.pipeNo = pipeNo;

        open(usbInstance, interfaceNo, pipeNo, false);
    }

    /**
     * USB �f�o�C�X�̃G���h�|�C���g�ɃA�N�Z�X���邽�߂̃I�u�W�F�N�g��
     * �I�[�o�[���b�v���[�h�ō쐬����B
     * 
     * @param interfaceNo �C���^�[�t�F�[�X�ԍ�
     * @param pipeNo �p�C�v�ԍ�
     * @param overlapped �I�[�o�[���b�v
     * @throws UsbException USB�f�o�C�X�Ɏw�肵���p�C�v������
     */
    public Pipe(Usb usb, int interfaceNo, int pipeNo, boolean overlapped)
        throws UsbException {

        this.usbInstance = usb.instance;

        this.interfaceNo = interfaceNo;
        this.pipeNo = pipeNo;

        open(usbInstance, interfaceNo, pipeNo, overlapped);
    }

    /**
     * �p�C�v���N���[�Y���܂��B
     */
    public native void close();

    /**
     * TODO
     */
    public native int read() throws UsbException;

    /**
     * �ǂݍ��݂܂��D
     */
    public native int read(byte[] b, int off, int len) throws UsbException;

    /**
     * TODO
     */
    public native void write(int b) throws UsbException;

    /**
     * �������݂܂��D
     */
    public native void write(byte[] b, int off, int len) throws UsbException;

    //-------------------------------------------------------------------------

    /** USB �̃n���h�� */
    private long usbInstance;

    /** �p�C�v�̃n���h�� */
    @SuppressWarnings("unused")
    private long instance;

    /**
     * �p�C�v���쐬���܂��B
     * 
     * @param usbHandle USB �̃n���h��
     * @param interfaceNo �C���^�[�t�F�[�X�ԍ�
     * @param pipeNo �p�C�v�ԍ�
     * @param overlapped �I�[�o�[���b�v
     * @throws UsbException USB�f�o�C�X�Ɏw�肵���p�C�v������
     */
    private native void open(long usbHandle,
                             int interfaceNo,
                             int pipeNo,
                             boolean overlapped)
        throws UsbException;

    /**
     * �p�C�v�����Z�b�g���܂��B
     */
    @SuppressWarnings("unused")
    private native void reset() throws UsbException;

    /**
     * �p�C�v���A�{�[�g���܂��B
     */
    @SuppressWarnings("unused")
    private native void abort() throws UsbException;

    /** */
    static {
        System.loadLibrary("UusbdWrapper");
    }
}

/* */
