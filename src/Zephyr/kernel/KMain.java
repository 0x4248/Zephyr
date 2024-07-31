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


public class KMain {
    private void init() {
        while (true) {
            printkSystem.printk(KERN_LEVEL.INFO, "Hello, World!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void kernel_init(String[] args) throws InterruptedException {
        printkSystem.printkInit();
        printkSystem.printk(KERN_LEVEL.INFO,"Booting Zephyr Kernel 1.0.0-dev ARM64");
        process init = new process("init", new KMain()::init);
    }
}