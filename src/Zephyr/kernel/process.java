// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * kernel/process.java
 *
 * process management.
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.kernel;

import Zephyr.kernel.printk.KERN_LEVEL;
import java.util.HashMap;

public class process {
    private static HashMap<Integer, process> processes = new HashMap<>();
    private static int nextPID = 1;
    private int PID;
    private String name;
    public Thread thread;

    public process(String name, Runnable function) {
        printkSystem.printk(KERN_LEVEL.INFO, "ProcessManager: Process " + name + " created on PID " + nextPID);
        this.PID = nextPID;
        this.name = name;
        this.thread = new Thread(function);
        processes.put(PID, this);
        nextPID++;
        thread.start();
    }

    public void stop() {
        printkSystem.printk(KERN_LEVEL.INFO, "ProcessManager: Process " + name + " stopped.");
        processes.remove(PID);
    }

    public static process getProcess(int PID) {
        return processes.get(PID);
    }

    public int getPID() {
        return PID;
    }

    public String getName() {
        return name;
    }

    public static HashMap<Integer, process> getProcesses() {
        return processes;
    }
}


