// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/Serial/Serial.java
 * Serial driver.
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.serial;

import Zephyr.drivers.device.Device;
import Zephyr.drivers.device.DeviceStream;

import java.io.IOException;

public class Serial {
    private static final Device serialDevice = new Device("Serial", new DeviceStream());
    public static int serialInit() {
        serialDevice.register();
        return 0;
    }
    public static void write(String msg) {
        try {
            serialDevice.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(msg);
    }

    public static String readBuffer() {
        byte[] buffer = new byte[1024];
        try {
            System.in.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buffer);
    }
}