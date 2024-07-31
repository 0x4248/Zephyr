// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/device/DeviceStream.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.device;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeviceStream extends ByteArrayOutputStream {
    @Override
    public void close() throws IOException {
        super.close();
    }

    public void reset() {
        this.reset();
    }
}