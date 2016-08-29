package com.jerry.bluetemperature.util;

public class Crc8 {

    public static byte getCrc8(byte[] data, int length) {
        if (data.length < length) {
            return 0;
        }

        byte crc = 0;
        int index = 0;

        while ((--length) > 0) {
            for (int i = 0x80; i != 0; i = i >> 1) {
                if ((crc & 0x80) != 0) {
                    crc <<= 1;
                    crc ^= 0x07;
                } else {
                    crc <<= 1;
                }

                if ((data[index] & i) != 0) {
                    crc ^= 0x07;
                }
            }

            index++;
        }

        return crc;
    }
}