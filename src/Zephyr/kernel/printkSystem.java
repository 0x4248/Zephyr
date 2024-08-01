// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * kernel/printkSystem.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.kernel;

import java.util.Date;

import Zephyr.drivers.serial.Serial;
import Zephyr.kernel.printk.KERN_LEVEL;

public class printkSystem {
    private static Date initTime;
    public static int printkInit() {
        initTime = new Date();
        Serial.serialInit();
        return 0;
    }
    public static void printk(KERN_LEVEL level, String msg) {
        long currentTime = new Date().getTime();

        String time = String.format("%1$,.6f", (currentTime - initTime.getTime()) / 1000.0);
        int timeLength = time.length()-7;
        String space = "     ";
        space = space.substring(timeLength);
        Serial.write("["+ space + time + "] " + msg + "\n");

    }
}
