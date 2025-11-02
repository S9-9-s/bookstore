package com.saida.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookDto(
        Long id,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        Integer publicationYear,
        LocalDateTime createdAt
) {
    public BookDto(String title, String author, String isbn, BigDecimal price, Integer publicationYear) {
        this(null, title, author, isbn, price, publicationYear, null);
    }
}