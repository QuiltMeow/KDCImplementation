package ew.sr.x1c.quilt.meow.kdc.simulator.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    private static final Random RAND = new SecureRandom();

    public static int nextInt() {
        return RAND.nextInt();
    }

    public static int nextInt(int max) {
        return RAND.nextInt(max);
    }

    public static void nextByte(byte[] input) {
        RAND.nextBytes(input);
    }

    public static boolean nextBoolean() {
        return RAND.nextBoolean();
    }

    public static double nextDouble() {
        return RAND.nextDouble();
    }

    public static float nextFloat() {
        return RAND.nextFloat();
    }

    public static long nextLong() {
        return RAND.nextLong();
    }

    public static int rand(int lowBound, int upBound) {
        return nextInt(upBound - lowBound + 1) + lowBound;
    }

    public static boolean isSuccess(int rate) {
        return rate > nextInt(100);
    }

    public static double nextDouble(double max) {
        return ThreadLocalRandom.current().nextDouble(max);
    }

    public static double randDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
