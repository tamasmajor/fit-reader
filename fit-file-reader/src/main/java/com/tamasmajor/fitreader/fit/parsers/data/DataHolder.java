package com.tamasmajor.fitreader.fit.parsers.data;

import lombok.Getter;

import java.util.Arrays;

public class DataHolder {
    private final byte[] data;

    @Getter
    private int currentPosition = 0;

    public DataHolder(byte[] input) {
        data = input;
    }

    public byte getCurrentByte() {
        return data[currentPosition];
    }

    public byte[] getNextBytes(int length) {
        if (currentPosition + length > data.length) {
            throw new IllegalArgumentException("Not enough data remaining");
        }
        return Arrays.copyOfRange(data, currentPosition, currentPosition + length);
    }

    public void movePositionBy(int value) {
        if (currentPosition + value < 0 || currentPosition + value > data.length) {
            throw new IllegalArgumentException("Illegal position: " + (currentPosition + value));
        }
        this.currentPosition += value;
    }

}
