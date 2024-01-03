package com.easysplit.ess.shared.utils;

import java.time.Instant;
import java.util.Random;

/**
 * Class with utility methods for testing
 */
public class TestUtils {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private TestUtils() {

    }

    /**
     * Generates a unique string based on the current timestamp, util to avoid creating
     * test object with exactly the same attributes values
     *
     * @return unique string
     */
    public static String generateUniqueString() {
        Instant now = Instant.now();
        long timestamp = now.toEpochMilli();

        return timestamp + "";
    }

    /**
     * Generates a 10-digit number, util to generate random cellphone numbers
     *
     * @return 10-digit number
     */
    public static int generate10DigitNumber() {
        Random random = new Random();
        return (int) (Math.floor(Math.pow(10, 9) + random.nextDouble() * 9 * Math.pow(10, 8)));
    }

    /**
     * Generates a random string with the specified length, util for negative test cases
     *
     * @param length string length
     * @return string of the specified length
     */
    public static String generateString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
