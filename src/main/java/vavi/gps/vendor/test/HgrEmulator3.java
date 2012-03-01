/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.TimerTask;
import vavi.util.Debug;


/**
 * HGR �̃G�~�����[�V�������s���N���X�ł��B
 * 
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	030410	nsano	initial version <br>
 */
public class HgrEmulator3 extends HgrEmulator {

    /** ������̔{���x */
    int ff = 10;

    /** �V���A������ւ� GPS �f�[�^�̏o�� */
    protected TimerTask getOutputTimerTask() {
        return new TimerTask() {
            public void run() {
                try {
                    for (int i = 0; i < ff - 1; i++) {
                        reader.readLine();
                    }
                    String line = reader.readLine();
                    os.writeLine(line);
                }
                catch (IOException e) {
Debug.println(e);
                }
            }
        };
    }

    //----

    /** */
    private String ioDeviceName = "dummy3";

    /** */
    protected String getIODeviceName() {
        return ioDeviceName;
    }

    /** */
    private static String file =
        System.getProperty("user.home") +
        System.getProperty("file.separator") +
        ".hgr.log";

    /** */
    private BufferedReader reader;

    /** */
    public HgrEmulator3() {

        final Class<?> c = HgrEmulator3.class;

        try {
            Properties props = new Properties();

            props.load(c.getResourceAsStream("DummyHgrDevice.properties"));

            String value = props.getProperty("emulator3.ff");
            if (value != null) {
Debug.println("ff: " + value);
		        ff = Integer.parseInt(value);
            }

            value = props.getProperty("emulator3.file");
            if (value != null) {
Debug.println("file: " + value);
		        file = value;
            }

            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
Debug.println(e);
	    System.exit(1);
        }
    }
}

/* */
