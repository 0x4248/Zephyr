// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * kernel/panicSystem.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.kernel;

import Zephyr.drivers.serial.Serial;

public class panicSystem {
    public static void panic(String msg) {
        Serial.write("--- [Kernel panic - message: " + msg+"]---\n");
        Serial.write("Stack trace:\n");
        Serial.write("Called from: "+Thread.currentThread().getStackTrace()[2].getClassName()+"\n");
        Serial.write("Function: "+Thread.currentThread().getStackTrace()[2].getMethodName()+"\n");
        Serial.write("Line: "+Thread.currentThread().getStackTrace()[2].getLineNumber()+"\n");
        for (int i = 2; i < Thread.currentThread().getStackTrace().length; i++) {
            Serial.write(Thread.currentThread().getStackTrace()[i].getClassName()+"."+Thread.currentThread().getStackTrace()[i].getMethodName()+"("+Thread.currentThread().getStackTrace()[i].getFileName()+":"+Thread.currentThread().getStackTrace()[i].getLineNumber()+")\n");
        }
        Serial.write("--[End kernel panic]--\n");
        Serial.write("System halted\n");
        while (true) {
        }
    }
}