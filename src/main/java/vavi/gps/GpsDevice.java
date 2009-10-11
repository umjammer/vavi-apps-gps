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
     * ���̓f�o�C�X�̃|�[�����O���s���X���b�h���擾���܂��B
     * �ȉ��� <code>{@link java.lang.Runnable#run()}</code> ���\�b�h�ɋL�q���邱�ƁB
     * <ol>
     * <li>���̓f�o�C�X����P�u���b�N��ǂݎ��</li>
     * <li>1. �̃f�[�^�� <code>{@link vavi.gps.GpsData}</code> �܂��͂��̃T�u�N���X��
     * �ϊ�����</li>
     * <li><code>{@link vavi.util.event.GenericEvent}</code> ��
     * argument �� 2. �̃f�[�^�Aname �� "data" �Ƃ��č쐬����</li>
     * <li>3. �̃C�x���g�������ɂ��� <code>fireEventHappened()</code>
     * ���\�b�h�𔭍s����</li>
     * </ol>
     * @return	java.lang.Runnable �̎����N���X
     */
    protected abstract Runnable getInputThread();

    /** TODO �����ڑ����Ή� */
    protected volatile boolean loop;

    /** �f�o�C�X���@�\�����܂��B */
    public void start() {
        loop = true;
        Thread thread = new Thread(getInputThread());
        thread.start();
    }

    /**
     * �o�̓f�o�C�X�փf�[�^���o�͂���A�C�x���g���X�i���擾���܂��B
     * �ȉ��� <code>{@link vavi.util.event.GenericListener#eventHappened(GenericEvent)}</code>
     * ���\�b�h�ɋL�q���邱�ƁB
     * <ol>
     * <li><code>{@link vavi.util.event.GenericEvent}</code> �� getName() �� "name"
     * �̃C�x���g�� getArgument() ���� <code>{@link vavi.gps.GpsData}</code> �^��
     * �f�[�^���擾����</li>
     * <li>1. �̃f�[�^���o�̓f�o�C�X�ɓK�؂ȃt�H�[�}�b�g�ɕϊ�����</li>
     * <li>�o�̓f�o�C�X�� 2. �̃f�[�^���o�͂���
     * </ol>
     * @return {@link vavi.util.event.GenericListener} �̎����N���X
     */
    protected abstract GenericListener getOutputGenericListener();

    /** ���̓f�o�C�X�Əo�̓f�o�C�X��ڑ����܂��B */
    public void connect(GpsDevice outputDevice) {
        // out@outputDevice -> in@inputDevice
        GenericListener listener = outputDevice.getOutputGenericListener();
        addGenericListener(listener);
        // out@inputDevice -> in@outputDevice
        listener = getOutputGenericListener();
        outputDevice.addGenericListener(listener);
    }

    //-------------------------------------------------------------------------

    /** �ėp�C�x���g�@�\�̎��� */
    private GenericSupport gs = new GenericSupport();

    /** �ėp�C�x���g���X�i��ǉ����܂��B */
    public void addGenericListener(GenericListener listener) {
        gs.addGenericListener(listener);
    }

    /** �ėp�C�x���g���X�i���폜���܂��B */
    public void removeGenericListener(GenericListener listener) {
        gs.removeGenericListener(listener);
    }

    /** �ėp�C�x���g�𔭍s���܂��B */
    protected void fireEventHappened(GenericEvent ev) {
        gs.fireEventHappened(ev);
    }
}

/* */
