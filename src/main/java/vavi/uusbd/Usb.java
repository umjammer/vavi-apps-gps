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

    /** */
    public Usb() throws IOException {
        open();
    }

    /** */
    public Usb(int flag,
               int clazz,
               int subClass,
               int vendor,
               int product,
               byte bcdDevice) throws IOException {
        open(flag, clazz, subClass, vendor, product, bcdDevice);
    }

    //-------------------------------------------------------------------------

    /** USB �̃n���h�� */
    long instance;

    /**
     * USB�f�o�C�X���I�[�v������B
     * �V�X�e���ɐڑ�����Ă��āAuusbd.sys ���h���C�o�[�Ƃ��Ďg�p����Ă���A
     * ���̃A�v���P�[�V�������I�[�v�����Ă��Ȃ� USB �f�o�C�X�����݂���K�v��
     * ����B�g�p�� close() ���Ăяo�����ƁB
     * @throws UsbException
     */
    private native void open() throws UsbException;

    /** */
    public static final int MASK_NO = 0;
    /** */
    public static final int MASK_CLASS = 1;
    /** */
    public static final int MASK_SUBCLASS = 2;
    /** */
    public static final int MASK_VENDOR = 4;
    /** */
    public static final int MASK_PRODUCT = 8;
    /** */
    public static final int MASK_BCDDEVICE = 16;

    /**
     * open() �Ɍ���������ǉ���������
     * �g�p�� close() ���Ăяo�����ƁB
     *
     * <li> TODO ���e�X�g
     *
     * @param flag �f�o�C�X���������Ɏg�p����������w�肷��B
     *             �����̍��ڂŏ��������߂�ꍇ��OR���Ƃ������̂��g��
     * <pre>
     *		MASK_CLASS �N���X�R�[�h�����������ɂ��鎞
     *		MASK_SUBCLASS �T�u�N���X�R�[�h�����������ɂ��鎞
     *		MASK_VENDOR �x���_�[ ID �����������ɂ��鎞
     *		MASK_PRODUCT	�v���_�N�g ID �����������ɂ��鎞
     *		MASK_BCDDEVICE �f�o�C�X�����[�X�ԍ������������ɂ��鎞
     *		MASK_NO ��L�̂�����̃}�X�N���g�p���Ȃ��ꍇ
     * </pre>
     * @param clazz	�f�o�C�X�̃N���X�R�[�h
     * @param subClass	�T�u�N���X�R�[�h
     * @param vendor	�x���_�[ID
     * @param product	�v���_�N�gID
     * @param bcdDevice	�f�o�C�X�����[�X�ԍ�
     */
    private native void open(int flag,
                             int clazz,
                             int subClass,
                             int vendor,
                             int product,
                             byte bcdDevice) throws UsbException;

    /**
     * �N���[�Y���܂��D
     */
    public native void close();

    /** */
    public static final int RECIPIENT_DEVICE = 0;
    /** */
    public static final int RECIPIENT_INTERFACE = 1;
    /** */
    public static final int RECIPIENT_ENDPOINT = 2;
    /** */
    public static final int RECIPIENT_OTHER = 3;

    /**
     * �N���X���N�G�X�g�𑗂�B
     * �f�[�^�[�]���𔺂�Ȃ����N�G�X�g�̏ꍇ�A
     * wLength �� 0, dir_in �� false �Ƃ��邱�ƁB
     *
     * @param dir_in	USB �f�o�C�X����f�[�^���󂯂�ꍇ true �ɂ���
     * @param recipient	RequestTyep �� D4..D0 �Ɏw�肷���M����w�肷��B
     *                  ���̂����ꂩ���w��B
     * <ul>
     *	<li>RECIPIENT_DEVICE	�f�o�C�X
     *	<li>RECIPIENT_INTERFACE	�C���^�[�t�F�[�X
     *	<li>RECIPIENT_ENDPOINT	�G���h�|�C���g
     *	<li>RECIPIENT_OTHER	���̑�
     * </ul>
     * @param bRequest	���N�G�X�g�̔ԍ�
     * @param wValue	bRequest �̒l�ɉ����� USB �f�o�C�X�Ƃ̊ԂŌ��߂�ꂽ
     *                  16bit �l
     * @param wIndex	bRequest�̒l�ɉ�����USB�f�o�C�X�Ƃ̊ԂŌ��߂�ꂽ
     *                  16bit �l�B������index �l��A�I�t�Z�b�g�l���킽���̂�
     *                  �g��
     * @param wLength	�f�[�^�[�𑗎�M����ꍇ�̓]����(byte)
     * @param data	�]���f�[�^�̏ꏊ���͎�M�f�[�^�̊i�[�ꏊ
     */
    public native void sendClassRequest(boolean dir_in,
                                        int recipient,
                                        int bRequest,
                                        int wValue,
                                        int wIndex,
                                        int wLength,
                                        byte[] data) throws UsbException;

    /**
     * �f�o�C�X���N�G�X�g�𑗂�B
     *
     * <li> TODO ���e�X�g
     */
    public native void sendVendorRequest(boolean dir_in,
                                         int recipient,
                                         int bRequest,
                                         int wValue,
                                         int wIndex,
                                         int wLength,
                                         byte[] data) throws UsbException;

    /**
     * USB �f�o�C�X�����݂��邩�ǂ����m�F����B
     * 
     * @return true �f�o�C�X�͑��݂��Đ���ɓ��삵�Ă���
     *         false �f�o�C�X�����O���ꂽ
     *         (�f�o�C�X�h���C�o�[�� remove ����Ă���)
     * @throws UsbException �����ł���
     */
    private native boolean available() throws UsbException;

    /**
     * USB �f�o�C�X�����Z�b�g����
     */
    private native void reset() throws UsbException;

    /**
     * USB �f�o�C�X�̃f�o�C�X�f�B�X�N���v�^�[�𓾂�
     */
//    private native DeviceDescriptor getDeviceDescriptor() throws UsbException;

    /**
     * USB �f�o�C�X�̃R���t�B�O���[�V�����f�B�X�N���v�^�[�𓾂�B
     */
//    private native ConfigurationDescriptor getConfigurationDescriptor() throws UsbException;

    /**
     * QualifierDescriptor �𓾂�
     */
//    private native DeviceQualifierDescriptor getDeviceQualifierDescriptor() throws UsbException;

    /**
     * �f�o�C�X�� OtherSpeedConfigurationDescriptor �𓾂�B
     * OtherSpeedConfigurationDescriptor �� ConfigurationDescriptor �Ɠ���
     * �t�H�[�}�b�g�ł���B
     * ��������e�� OtherSpeedConfigurationDescriptor �ł��邱�ƈȊO�́A
     * getConfigurationDescriptor �Ɠ��l�B 
     */
//    private native ConfigurationDescriptor getOtherSpeedConfigurationDescriptor() throws UsbException;

    // ------------------------------------------------------------------------

    /** */
    static {
        System.loadLibrary("UusbdWrapper");
    }
}

/* */
