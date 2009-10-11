/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps;


/**
 * GPS Format.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public interface GpsFormat {
    /**
     * Raw �f�[�^���o�͂�����\��������ꍇ�͖߂�l�� {@link GpsData} ��
     * {@link BasicGpsData#setRawData(byte[])} ���\�b�h�� line ��ݒ肵�Ă����Ă��������B
     */
    GpsData parse(byte[] line);

    /** */
    byte[] format(GpsData gpsData);
}

/* */
