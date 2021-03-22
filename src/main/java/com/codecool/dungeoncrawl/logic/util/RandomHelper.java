package com.codecool.dungeoncrawl.logic.util;

import java.util.Random;

public class RandomHelper {
    private RandomHelper() {}

    public static int getRandomNumber(int lowerBound, int upperBound) {
        Random random = new Random();
        return lowerBound + random.nextInt(upperBound - lowerBound);
    }

    public static String getRandomName(String[] possibilities) {
        return possibilities[getRandomNumber(0, possibilities.length)];
    }
}
