// SPDX-License-Identifier: GPL-3.0-only
/* Zephyr
 * A simple kernel simulation.
 *
 * drivers/BBFS/BBFS.java
 * BBFS driver.
 *
 * Copyright (C) 2024 0x4248
 */

package Zephyr.drivers.BBFS;

import java.io.*;
import java.util.*;

import Zephyr.kernel.printkSystem;
import Zephyr.kernel.printk.KERN_LEVEL;

public class BBFS {
    private static final int BLOCK_SIZE = 4096;

    private List<FileEntry> fileEntries = new ArrayList<>();
    private File diskImage;

    static class FileEntry {
        String name;
        int size;
        int startBlock;

        public FileEntry(String name, int size, int startBlock) {
            this.name = name;
            this.size = size;
            this.startBlock = startBlock;
        }

        @Override
        public String toString() {
            return String.format("%-16s %-8d %-8d", name, size, startBlock);
        }
    }

    public BBFS(String diskImagePath) throws IOException {
        diskImage = new File(diskImagePath);
        if (diskImage.exists()) {
            loadFileTable();
        } else {
            diskImage.createNewFile();
        }
    }

    private void loadFileTable() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(diskImage, "r")) {
            raf.seek(diskImage.length() - BLOCK_SIZE);
            byte[] buffer = new byte[BLOCK_SIZE];
            while (raf.read(buffer) != -1) {
                String name = new String(buffer, 0, 16).trim();
                int size = byteArrayToInt(Arrays.copyOfRange(buffer, 16, 20));
                int startBlock = byteArrayToInt(Arrays.copyOfRange(buffer, 20, 24));
                if (size == 0 && startBlock == 0) {
                    break;
                }
                fileEntries.add(new FileEntry(name, size, startBlock));
            }
        }
    }

    public ArrayList<String> ls() {
        ArrayList<String> files = new ArrayList<>();
        for (FileEntry entry : fileEntries) {
            files.add(entry.name);
        }
        return files;
    }

    public void read(String fileName) throws IOException {
        for (FileEntry entry : fileEntries) {
            if (entry.name.equals(fileName)) {
                try (RandomAccessFile raf = new RandomAccessFile(diskImage, "r")) {
                    raf.seek(entry.startBlock * BLOCK_SIZE);
                    byte[] fileContent = new byte[entry.size];
                    raf.read(fileContent);
                    printkSystem.printk(KERN_LEVEL.TRACE, "BBFS: Data read from file " + fileName);
                    return;
                }
            }
        }
        printkSystem.printk(KERN_LEVEL.ERROR, "BBFS: File not found: " + fileName);
    }

    public void write(String fileName, String content) throws IOException {
        int size = content.getBytes().length;
        int blocksNeeded = (int) Math.ceil((double) size / BLOCK_SIZE);

        FileEntry existingEntry = null;
        for (FileEntry entry : fileEntries) {
            if (entry.name.equals(fileName)) {
                existingEntry = entry;
                break;
            }
        }

        if (existingEntry != null) {
            try (RandomAccessFile raf = new RandomAccessFile(diskImage, "rw")) {
                raf.seek(existingEntry.startBlock * BLOCK_SIZE);
                raf.write(content.getBytes());
                if (size % BLOCK_SIZE != 0) {
                    byte[] padding = new byte[BLOCK_SIZE - (size % BLOCK_SIZE)];
                    raf.write(padding);
                }
            }
            existingEntry.size = size;
        } else {
            int startBlock = fileEntries.isEmpty() ? 0 : fileEntries.get(fileEntries.size() - 1).startBlock + blocksNeeded;
            FileEntry newEntry = new FileEntry(fileName, size, startBlock);
            fileEntries.add(newEntry);

            try (RandomAccessFile raf = new RandomAccessFile(diskImage, "rw")) {
                raf.seek(startBlock * BLOCK_SIZE);
                raf.write(content.getBytes());
                if (size % BLOCK_SIZE != 0) {
                    byte[] padding = new byte[BLOCK_SIZE - (size % BLOCK_SIZE)];
                    raf.write(padding);
                }
            }
        }

        updateFileTable();

        printkSystem.printk(KERN_LEVEL.TRACE, "BBFS: Data written to file " + fileName);
    }

    public void delete(String fileName) throws IOException {
        FileEntry toDelete = null;
        for (FileEntry entry : fileEntries) {
            if (entry.name.equals(fileName)) {
                toDelete = entry;
                break;
            }
        }

        if (toDelete != null) {
            fileEntries.remove(toDelete);
            updateFileTable();
        } else {
            printkSystem.printk(KERN_LEVEL.ERROR, "BBFS: File not found: " + fileName);
        }
    }

    private void updateFileTable() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(diskImage, "rw")) {
            raf.seek(fileEntries.get(fileEntries.size() - 1).startBlock * BLOCK_SIZE + fileEntries.get(fileEntries.size() - 1).size);
            for (FileEntry entry : fileEntries) {
                byte[] nameBytes = Arrays.copyOf(entry.name.getBytes(), 16);
                raf.write(nameBytes);
                raf.write(intToByteArray(entry.size));
                raf.write(intToByteArray(entry.startBlock));
            }

            byte[] eofMarker = new byte[BLOCK_SIZE];
            raf.write(eofMarker);
        }
    }

    private static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }

    private static int byteArrayToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                (bytes[3] & 0xFF);
    }
}
