package com.saida.bookstore.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookResponse(
        UUID publicId,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        Integer publicationYear,
        LocalDateTime createdAt
) {
}