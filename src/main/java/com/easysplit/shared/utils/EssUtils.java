package com.easysplit.shared.utils;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Class to implement utility methods shared by all the resources
 */
public final class EssUtils {
    /**
     * Private constructor to prevent instantiation of the class
     * This class should have static methods only
     */
    private EssUtils() {

    }

    /**
     * Returns true if the collection is null or empty
     *
     * @param collection collection to be validated
     * @return true if the collection is null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Returns true if the string is null or empty
     *
     * @param s string to be validated
     * @return true if the string is null or empty
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
