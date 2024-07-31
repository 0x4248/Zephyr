// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/device/Device.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.device;

import java.io.IOException;
import Zephyr.kernel.printkSystem;
import Zephyr.kernel.printk.KERN_LEVEL;

public class Device {
    private String name;
    private DeviceStream content;
    private boolean locked;

    public Device(String name, DeviceStream content) {
        this.name = name;
        this.content = content;
        this.locked = false;
    }

    public void register() {
        printkSystem.printk(KERN_LEVEL.INFO, "DeviceManager: Device " + name + " registered.");
        DeviceManager.registerDevice(this);
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public int write(byte[] data) throws IOException {
        if (!locked) {
            content.write(data);
            return 0;
        } else {
            return -1;
        }
    }

    public byte[] dumpContent() {
        return content.toByteArray();
    }

    public byte[] read(int bufferFrom, int bufferTo) {
        byte[] contentArray = content.toByteArray();
        if (bufferFrom >= 0 && bufferTo <= contentArray.length && bufferFrom <= bufferTo) {
            byte[] buffer = new byte[bufferTo - bufferFrom];
            System.arraycopy(contentArray, bufferFrom, buffer, 0, buffer.length);
            return buffer;
        } else {
            throw new IllegalArgumentException("Invalid buffer range.");
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void unregister() throws IOException {
        close();
        DeviceManager.unregisterDevice(name);
        printkSystem.printk(KERN_LEVEL.INFO, "DeviceManager: Device " + name + " unregistered.");
    }

    public void reset() {
        content.reset();
    }

    public void close() throws IOException {
        content.close();
    }

    public String getName() {
        return name;
    }
}
