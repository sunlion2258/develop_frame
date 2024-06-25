package com.sun.dev.nuidemo;

public interface IMainRecorderCallback {
    int onRecorderData(byte[] data, int len, boolean first_pack);
}
