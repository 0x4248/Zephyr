// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/console/Console.java
 * Console driver.
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.console;

import Zephyr.drivers.serial.Serial;

public class Console {
    public static void putChar(char c) {
        Serial.write(String.valueOf(c));
    }

    public static void putString(String s) {
        Serial.write(s);
    }

    public static void printf(String format, Object... args) {
        Serial.write(String.format(format, args));
    }

    public static String readLine() {
        return Serial.readBuffer();
    }
}
