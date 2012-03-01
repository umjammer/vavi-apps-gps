/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import vavi.gps.GpsDevice;
import vavi.io.IODevice;
import vavi.io.IODeviceInputStream;
import vavi.io.IODeviceOutputStream;
import vavi.util.event.GenericListener;
import vavi.util.Debug;


/**
 * HGR �̃G�~�����[�V�������s���N���X�ł��B
 * <pre>
 * <!PUON		�d�� ON
 * > ���b�Z�[�W
 * < !PUOFF		�d�� OFF
 * > ����
 * < !PC		GPS OFF
 * > OK
 * < !GP		GPS ON
 * > OK
 * < !ID		ID �擾
 * > ID ���
 * < !MRD6		���������W�X�^�ǂݍ��� D6 ���W�X�^
 * > MRDT3e900	... 0x3e900
 * < !MWW1c,21c	���������W�X�^�������� W1c ���W�X�^�A�l 0x21c
 * > OK		or NG
 * < !MD100,4c		[W0],[Da]
 * > MDC#......
 * > MDC#...
 * >  :
 * > MDF#...
 * </pre>
 * ���W�X�^�̐���
 * <pre>
 * W0	�������X�^�[�g�I�t�Z�b�g�H(�ǂݍ��ݐ�p)
 *
 *		���� 0x100
 *
 * D2	�������X�^�[�g�I�t�Z�b�g�H(�ǂݍ��ݐ�p)
 *
 *		���� 0x100
 *
 * D6	���ڃ�������(�ǂݍ��ݐ�p)
 *
 *		HGR3 �ł� 0x3e900
 *
 * Da	�������g�p��
 *
 *		���ʋL�^1�n�_������ 19 byte ���������g�p����̂ŁA
 *		Da �œǂݎ�����l�� 19 �Ŋ���� �L�^����Ă��鑪�ʃf�[�^�̐�
 *		���킩��B
 *		�������N���A���������͂��̃��W�X�^�� 0 �� set ����B
 * ��:
 * 	!MRDa
 *	 MRDTa3e      �� 0xa3e = 2622 = 138*19
 *	 !MWDa,0      ���������N���A
 *	 OK
 *
 * De	�s�� (�ǂݍ��ݐ�p)
 *
 *		HGR3 �ł͂��� 0x384e6
 *		(256 Kbyte �̓��ڃ���������A���}�i�b�N�f�[�^�Ȃǂ̕ۑ��Ɏg��
 *		 �������̕����������A���ۂɗ��p�ł��郁�����ʁH
 *
 * D16	�s�� (�ǂݍ��ݐ�p)
 *
 *		HGR3 �ł͂��� 0
 *
 * W12	�P�̓��쎞 ���ʃf�[�^�L�^�Ԋu
 *
 *		0 �̎��A���ʃf�[�^�̎����L�^�����Ȃ��B
 *		(�}�[�N�{�^�����������������L�^�����)
 * ��:
 *	 !MRW12
 *	 MRDT1	�� 1�b
 *	 !MWW12,3c    �� 0x3c = 60(�b)�ɐݒ�
 *	 OK
 *
 * W14	�s�� (�ǂݍ��ݐ�p)
 *
 *		HGR3 �ł͂��� 0
 *
 * W1a	PC�ڑ��� ���ʃf�[�^�L�^�Ԋu
 *
 *		0 �̎��A���ʃf�[�^�̎����L�^�����Ȃ��B
 *		(�}�[�N�{�^�����������������L�^�����)
 * ��:
 *	 !MRW1a
 *	 MRDT1	�� 0 (�����L�^�Ȃ�)
 *	 !MWW1a,1     �� 1�b�ɐݒ�
 *	 OK
 *
 * W1c	LCD �̎����\�� Timezone offset (HGR3 only?)
 *
 *		HGR �����ł� GMT(UTC) �ň����Ă��āA���̃��W�X�^��
 *		�l���I�t�Z�b�g ����ĉt���Ɏ����\�������B�P�ʂ́u���v
 *		�ʏ���{�Ȃ�� 60*9 = 540 = 0x21c
 *
 *	 !MRW1c
 *	 MRDT21c      �� 0x21c = 540 = 60*9 (JST)
 *	 !MWW1c,0     �� 0 (GMT)
 *	 OK
 *	 !MWW1c,fed4  �� 0xfed4 = -300 (short��) = -5*60
 *
 * B1e	�s��
 *
 *		HGR3 �ł͂��� 1 ���ǂݏ��������
 *
 * B1f	�s��
 *
 *		HGR3 �ł͂��� 5 ���ǂݏ��������
 *
 * B20	�o�b�N���C�g�_������ (HGR3 only?)
 *
 *		0 �̎��A�o�b�N���C�g���� OFF �������ƂȂ�B
 *
 *	��:
 *		!MRB20
 *		MRDT0	��  0 (�o�b�N���C�g����OFF������)
 *		!MWB20,78    ��  0x78 = 120 (�b)�ɐݒ�
 *		OK
 *
 * ���Ԃ�擪�̃A���t�@�x�b�g
 *  B Byte, W Word, D DWord
 * </pre>
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030331 nsano initial version <br>
 */
public abstract class HgrEmulator extends GpsDevice {

    /** */
//  private int register_W1a = 0;

    //----

    /** */
    private boolean powerOn = false;

    /** */
    private Timer outputTimer;

    /** */
    private long interval = 1000;

    /** */
    private void doCommand(String command) throws IOException {
        if ("!PUON".equals(command)) {
            doPUON();
        } else if ("!PUOFF".equals(command)) {
            doPUOFF();
        } else if ("!GP".equals(command)) {
            doGP();
        } else if ("!PC".equals(command)) {
            doPC();
        } else if ("!ID".equals(command)) {
            doID();
        } else {
Debug.println("unrecognized command: " + command);
        }
    }

    /** */
    private void doPUON() throws IOException {
        os.writeLine("ROM    OK");
        os.writeLine("RS232C OK");
        os.writeLine("CLOCK  NG");
        os.writeLine();
        os.writeLine("        ----< SONY GLOBAL POSITIONING SYSTEM >-----");
        os.writeLine("                               (C)Copyright 1991,1997   Sony Corporation.");
        os.writeLine();

        powerOn = true;
    }

    /** */
    private void doPUOFF() throws IOException {
        if (!powerOn) {
            return;
        }

        if (outputTimer != null) {
            outputTimer.cancel();
        }
        powerOn = false;
Debug.println("power off");
    }

    /** */
    private void doPC() throws IOException {
        if (!powerOn) {
            return;
        }

        if (outputTimer != null) {
            outputTimer.cancel();
        }

        os.writeLine("OK");
    }

    /** */
    private void doGP() throws IOException {
        if (!powerOn) {
            return;
        }

        outputTimer = new Timer();
        outputTimer.schedule(getOutputTimerTask(), 100, interval);

        os.writeLine("OK");
    }

    /** */
    private void doID() throws IOException {
        os.writeLine("ID" + "DTPCQ-HGR3,1.0.00.07281");
    }

    //----

    /** */
    protected IODeviceInputStream is;

    /** */
    protected IODeviceOutputStream os;

    /** �V���A���������̓��� */
    protected Runnable getInputThread() {
        return new Runnable() {
            public void run() {
Debug.println("input thread started");
                while (loop) {
                    try {
                        String command = is.readLine();
//Debug.println("command: " + command);
                        doCommand(command);
                    }
                    catch (IOException e) {
Debug.println(e);
                    }
                }
Debug.println("input thread stopped");
            }
        };
    }

    /** */
    protected GenericListener getOutputGenericListener() {
        throw new IllegalStateException("This class cannot be output device.");
    }

    /** �V���A������ւ� GPS �f�[�^�̏o�� */
    protected abstract TimerTask getOutputTimerTask();

    /** make sure not to duplicate input thread */
    public void start() {
Debug.println("here");
    }

    /** */
    public HgrEmulator() {

        IODevice ioDevice = new SharedMemoryDevice("output", "input");

        this.is = new IODeviceInputStream(ioDevice);
        this.os = new IODeviceOutputStream(ioDevice);

        super.start();
    }
}

/* */
