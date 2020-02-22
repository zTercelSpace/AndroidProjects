package com.ztercelstuido.SmartClassUtils;

public class SmartClassUtils {

    public static class BytePrinter {

        public static String byte2Hex(Byte inByte) {
            return String.format("%02x", inByte).toUpperCase();
        }

        public static String byteArrayToHex(byte[] bytes, int length) {
            StringBuilder strBuilder = new StringBuilder();
            int size = (-1 == length) ? bytes.length : length;
            for (int ii = 0; ii < size; ii++) {
                strBuilder.append(byte2Hex(bytes[ii]));
                strBuilder.append(" ");
            }
            return strBuilder.toString();
        }
    }
}