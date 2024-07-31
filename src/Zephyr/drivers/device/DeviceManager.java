// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/device/DeviceManager.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.device;

import java.util.*;

public class DeviceManager {
    private static Map<String, Device> devices = new HashMap<>();

    public static void registerDevice(Device device) {
        if (!devices.containsKey(device.getName())) {
            devices.put(device.getName(), device);
            System.out.println("Device " + device.getName() + " registered.");
        } else {
            System.out.println("Device " + device.getName() + " is already registered.");
        }
    }

    public static void unregisterDevice(String name) {
        if (devices.containsKey(name)) {
            devices.remove(name);
            System.out.println("Device " + name + " unregistered.");
        } else {
            System.out.println("Device " + name + " is not registered.");
        }
    }

    public static Device getDevice(String name) {
        return devices.get(name);
    }
}