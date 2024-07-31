// SPDX-License-Identifier: GPL-3.0-only
/* JUBK
 * Java Universal Boot Kit
 *
 * Copyright (C) 2024 0x4248
 */

package JUBK;

public class Main {
    public static void main(String[] args) {
        String kernelClassName = "Zephyr.kernel.KMain";

        for (String arg : args) {
            if (arg.startsWith("-kernel=")) {
                kernelClassName = arg.substring("-kernel=".length());
                break;
            }
        }
        System.out.println("Starting JUBK with kernel: " + kernelClassName);
        try {
            Class<?> kernelClass = Class.forName(kernelClassName);
            System.out.println("Kernel class loaded: " + kernelClassName);

            java.lang.reflect.Method kernelInitMethod = kernelClass.getMethod("kernel_init", String[].class);
            System.out.println("kernel_init method found in class: " + kernelClassName);

            kernelInitMethod.invoke(null, (Object) args);
        } catch (ClassNotFoundException e) {
            System.err.println("Kernel class not found: " + kernelClassName);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.err.println("No suitable kernel_init method found in class: " + kernelClassName);
            e.printStackTrace();
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            System.err.println("Error invoking kernel_init method.");
            e.printStackTrace();
        }
    }
}
