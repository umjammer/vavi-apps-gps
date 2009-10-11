/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.sony;

import vavi.gps.BasicGpsData;
import vavi.gps.Channel;


/**
 * IPS GPS data.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 *          0.01 030326 nsano extends BasicGpsData <br>
 */
public class IpsGpsData extends BasicGpsData {

    /** */
    protected boolean ready;

    /** */
    public boolean ready() {
        return ready;
    }

    /** */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /** ����M�킪 UNLOCK */
    public static final int TCXO_UNLOCKED = -9999;

    /** ��������M��̏�� */
    private int differenceOfClock;

    /** */
    public void setDifferenceOfClock(int differenceOfClock) {
        this.differenceOfClock = differenceOfClock;
    }

    /** */
    public int getDifferenceOfClock() {
        return differenceOfClock;
    }

    /** �s�� */
    byte[] unknown = new byte[2];

    public void setUnknown(byte[] unknown) {
        System.arraycopy(unknown, 0, this.unknown, 0, 2);
    }

    /** */
    public static final int UNIT_DMS = 0;

    /** */
    public static final int UNIT_DMD = 1;

    /** �o�x�ƈܓx�̒P�� */
    private int unitType;

    /** */
    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    /** */
    public int getUnitType() {
        return unitType;
    }

    /** */
    public static final int PARITY_EVEN = 0;

    /** */
    public static final int PARITY_ODD = 1;

    /** �p���e�B */
    public int getParity(byte[] data) {
        int sum = 0;
        for (int i = 0; i < 147; i++) {
            sum += data[i];
        }
        return (sum & 0x0001) != 0 ? PARITY_ODD : PARITY_EVEN;
    }

    //----

    /** */
    public int getMaxChannels() {
        return 16;
    }

    /** */
    public void addChannel(Channel channel) {
        String key = channel.getKeyString();
        if (!channels.containsKey(key)) {
            super.addChannel(channel);
        }
        else {
            Channel oldChannel = channels.get(key);
            if (oldChannel.getInfo() != Channel.INFO_OK) {
                super.addChannel(channel);
            }
        }
    }
}

/* */
