package ru.ifmo.rain.nefedov.walk;

import java.io.BufferedWriter;
import java.io.IOException;

public class Walk extends BaseWalk {
    public static void main(String[] args) {
        (new Walk()).walk(args);
    }

    @Override
    protected void processPathStr(String filePathStr, BufferedWriter bufferedWriter) throws IOException {
        processFile(filePathStr, bufferedWriter);
    }
}