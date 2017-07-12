package com.sge.mobile.domain.core;

/**
 * Created by Daniel on 05/07/13.
 */

/**
 * Utility code for domain classes.
 */
public class DomainObjectUtils {

    /**
     * Prevent instantiation.
     */
    private DomainObjectUtils() {
    }

    /**
     * @param actual actual value
     * @param safe   a null-safe value
     * @param <T>    type
     * @return actual value, if it's not null, or safe value if the actual value is null.
     */
    public static <T> T nullSafe(T actual, T safe) {
        return actual == null ? safe : actual;
    }

    public static boolean textHasContent(String aText) {
        String EMPTY_STRING = "";
        return (aText != null) && (!aText.trim().equals(EMPTY_STRING));
    }
}