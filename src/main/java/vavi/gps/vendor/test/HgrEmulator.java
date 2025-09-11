/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.gps.vendor.test;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Timer;
import java.util.TimerTask;
import vavi.gps.GpsDevice;
import vavi.io.IODevice;
import vavi.io.IODeviceInputStream;
import vavi.io.IODeviceOutputStream;
import vavi.util.event.GenericListener;

import static java.lang.System.getLogger;


/**
 * This class emulates HGR.
 * <pre>
 * <!PUON           power ON
 * > message
 * < !PUOFF         power OFF
 * > none
 * < !PC            GPS OFF
 * > OK
 * < !GP            GPS ON
 * > OK
 * < !ID            ID acquisition
 * > ID information
 * < !MRD6          Memory register read D6 register
 * > MRDT3e900      ... 0x3e900
 * < !MWW1c,21c     Write Memory Register W1c register, value 0x21c
 * > OK             or NG
 * < !MD100,4c      [W0],[Da]
 * > MDC#......
 * > MDC#...
 * >  :
 * > MDF#...
 * </pre>
 * Register Description
 * <pre>
 * W0    Memory start offset? (Read only)
 *
 *        Always 0x100
 *
 * D2    Memory start offset? (Read only)
 *
 *        Always 0x100
 *
 * D6    Amount of installed memory (read-only)
 *
 *        0x3e900 for HGR3
 *
 * Da   Memory usage
 *
 *        Each recorded position uses 19 bytes of memory,
 *        so if you divide the value read by Da by 19,
 *        you can find the number of recorded position data.
 *        To clear the memory, set this register to 0.
 * example:
 *     !MRDa
 *     MRDTa3e      → 0xa3e = 2622 = 138*19
 *     !MWDa,0      ← Memory Clear
 *     OK
 *
 * De    Unknown (read-only)
 *
 *        Always 0x384e6 on HGR3
 *        (What is the actual available memory amount,
 *         minus the memory used to store almanac data, etc.,
 *         from the 256 Kbytes of installed memory?
 *
 * D16    Unknown (read-only)
 *
 *        Always 0 in HGR3
 *
 * W12    Positioning data recording interval when operating alone
 *
 *        When set to 0, positioning data is not automatically recorded.
 *        (Recording only occurs when you press the mark button)
 * example:
 *     !MRW12
 *     MRDT1        → 1 second
 *     !MWW12,3c    → 0x3c = Set to 60 (seconds)
 *     OK
 *
 * W14    Unknown (read-only)
 *
 *        Always 0 in HGR3
 *
 * W1a    When connected to a PC, positioning data recording interval
 *
 *        When set to 0, positioning data is not automatically recorded.
 *        (Recording only occurs when you press the mark button)
 * example:
 *     !MRW1a
 *     MRDT1        → 0 (No automatic recording)
 *     !MWW1a,1     → Set to 1 second
 *     OK
 *
 * W1c    LCD time display Timezone offset (HGR3 only?)
 *
 *        The HGR uses GMT (UTC) internally, and the time is displayed
 *        on the LCD screen with an offset of this register value. The unit is minutes.
 *        In Japan, this is usually 60*9 = 540 = 0x21c.
 *
 *     !MRW1c
 *     MRDT21c      → 0x21c = 540 = 60*9 (JST)
 *     !MWW1c,0     → 0 (GMT)
 *     OK
 *     !MWW1c,fed4  → 0xfed4 = -300 (short) = -5*60
 *
 * B1e    Unknown
 *
 *        HGR3 always reads and writes 1
 *
 * B1f    Unknown
 *
 *        HGR3 always reads and writes 5
 *
 * B20    Backlight duration (HGR3 only?)
 *
 *        When set to 0, automatic backlight OFF is disabled.
 *
 *    example:
 *        !MRB20
 *        MRDT0        →  0 (Backlight auto-off disabled)
 *        !MWB20,78    →  0x78 = Set to 120 (seconds)
 *        OK
 *
 * Maybe the first alphabet
 *  B Byte, W Word, D DWord
 * </pre>
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030331 nsano initial version <br>
 */
public abstract class HgrEmulator extends GpsDevice {

    private static final Logger logger = getLogger(HgrEmulator.class.getName());

//    /** */
//    private int register_W1a = 0;

    // ----

    /** */
    private boolean powerOn = false;

    /** */
    private Timer outputTimer;

    /** */
    private static final long interval = 1000;

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
logger.log(Level.DEBUG, "unrecognized command: " + command);
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
logger.log(Level.DEBUG, "power off");
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

    // ----

    /** */
    protected final IODeviceInputStream is;

    /** */
    protected final IODeviceOutputStream os;

    @Override
    protected Runnable getInputThread() {
        return () -> {
logger.log(Level.DEBUG, "input thread started");
            while (loop) {
                try {
                    String command = is.readLine();
//logger.log(Level.DEBUG, "command: " + command);
                    doCommand(command);
                } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
logger.log(Level.DEBUG, "input thread stopped");
        };
    }

    @Override
    protected GenericListener getOutputGenericListener() {
        throw new IllegalStateException("This class cannot be output device.");
    }

    /** Outputting GPS data to a serial line */
    protected abstract TimerTask getOutputTimerTask();

    /** make sure not to duplicate input thread */
    @Override
    public void start() {
logger.log(Level.DEBUG, "here");
    }

    /** */
    public HgrEmulator() {

        IODevice ioDevice = new SharedMemoryDevice("output", "input");

        this.is = new IODeviceInputStream(ioDevice);
        this.os = new IODeviceOutputStream(ioDevice);

        super.start();
    }
}
