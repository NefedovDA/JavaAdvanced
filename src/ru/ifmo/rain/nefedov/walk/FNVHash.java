package ru.ifmo.rain.nefedov.walk;

public class FNVHash {
    static public final long INITIAL_HASH = 0x811c9dc5L;

    static private final long PRIMATE_32 = 0x01000193L;
    static private final long MOD_32 = 0xFFFFFFFFL;
    static private final long FROM_BYTE = 0xFFL;


    static long getHash(long hash, byte[] buffer, int bufferLength) {
        for (int i = 0; i < bufferLength; ++i) {
            hash *= PRIMATE_32;
            hash ^= (FROM_BYTE & buffer[i]);
            hash &= MOD_32;
        }
        return hash;
    }
}
