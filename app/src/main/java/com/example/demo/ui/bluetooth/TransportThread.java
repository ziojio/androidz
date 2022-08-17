package com.example.demo.ui.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;

class TransportThread extends Thread {
    private static final String TAG = "TransportThread";
    private final BluetoothSocket mBluetoothSocket;

    TransportThread(BluetoothSocket bluetoothSocket) {
        this.mBluetoothSocket = bluetoothSocket;
    }

    boolean isClosed = false;

    void close() {
        isClosed = true;
    }

    @Override
    public void run() {
        InputStream in = null;
        try {
            in = mBluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!isClosed) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}