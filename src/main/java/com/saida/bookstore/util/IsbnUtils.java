package com.saida.bookstore.util;

import org.apache.logging.log4j.util.Strings;

/**
 * Utility class for ISBN validation.
 */
public final class IsbnUtils {

    /**
     * Validates both ISBN-10 and ISBN-13 formats.
     * <p>
     * Examples:
     * - ISBN-10: "1234567890", "123456789X"
     * - ISBN-13: "9781234567890", "1234567890123"
     */
    public static final String ISBN_PATTERN = "^(?:\\d{9}[\\dXx]|\\d{13})$";

    private IsbnUtils() {
        // Utility class
    }

    /**
     * Checks if string is valid ISBN-10 or ISBN-13.
     */
    public static boolean isValidFormat(String isbn) {
        return isbn != null && isbn.matches(ISBN_PATTERN);
    }

    /**
     * Removes hyphens and converts to uppercase.
     */
    public static String normalize(String isbn) {
        if (isbn == null) {
            return null;
        }
        return isbn.replace("-", Strings.EMPTY).toUpperCase();
    }
}