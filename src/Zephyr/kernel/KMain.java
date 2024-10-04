// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * kernel/KMain.java
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.kernel;


import Zephyr.kernel.printk.KERN_LEVEL;
import Zephyr.drivers.BBFS.*;
import JBSH.Main;

public class KMain {
    public static void kernel_init(String[] args) throws InterruptedException {
        printkSystem.printkInit();
        printkSystem.printk(KERN_LEVEL.INFO,"Booting Zephyr Kernel 1.0.0-dev ARM64");
        process init = new process("init", () -> {
            Main.main(args);
        });

        printkSystem.printk(KERN_LEVEL.INFO, "Init process started: PID " + init.getPID());

        System.out.println(process.getProcesses());
    }
}