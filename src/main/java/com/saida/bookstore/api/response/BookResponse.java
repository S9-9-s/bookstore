package com.saida.bookstore.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        Integer publicationYear,
        LocalDateTime createdAt
) {
}