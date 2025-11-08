package com.saida.bookstore.util;

import org.apache.logging.log4j.util.Strings;

/**
 * Утилитный класс для валидации ISBN.
 * Содержит методы для проверки формата и нормализации ISBN номеров.
 */
public final class IsbnUtils {

    /**
     * Регулярное выражение для проверки форматов ISBN-10 и ISBN-13.
     * <p>
     * Примеры валидных ISBN:
     * - ISBN-10: "1234567890", "123456789X"
     * - ISBN-13: "9781234567890", "1234567890123"
     */
    public static final String ISBN_PATTERN = "^(?:\\d{9}[\\dXx]|\\d{13})$";

    private IsbnUtils() {
        // Утилитный класс
    }

    /**
     * Проверяет, соответствует ли строка формату ISBN-10 или ISBN-13.
     *
     * @param isbn строка для проверки
     * @return true если строка соответствует формату ISBN, false в противном случае
     */
    public static boolean isValidFormat(String isbn) {
        return isbn != null && isbn.matches(ISBN_PATTERN);
    }

    /**
     * Нормализует ISBN: удаляет дефисы и преобразует в верхний регистр.
     *
     * @param isbn ISBN строка для нормализации
     * @return нормализованная ISBN строка или null если входная строка null
     */
    public static String normalize(String isbn) {
        if (isbn == null) {
            return null;
        }
        return isbn.replace("-", Strings.EMPTY).toUpperCase();
    }
}