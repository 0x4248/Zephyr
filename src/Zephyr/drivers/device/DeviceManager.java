// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/device/DeviceManager.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.device;

import Zephyr.kernel.printkSystem;
import Zephyr.kernel.printk.KERN_LEVEL;

import java.util.*;

public class DeviceManager {
    private static Map<String, Device> devices = new HashMap<>();

    public static void registerDevice(Device device) {
        if (!devices.containsKey(device.getName())) {
            devices.put(device.getName(), device);
        } else {
            printkSystem.printk(KERN_LEVEL.ERROR, "Device " + device.getName() + " already registered.");
        }
    }

    public static void unregisterDevice(String name) {
        if (devices.containsKey(name)) {
            devices.remove(name);
        } else {
            printkSystem.printk(KERN_LEVEL.ERROR, "Device " + name + " not found.");
        }
    }

    public static Device getDevice(String name) {
        return devices.get(name);
    }
}