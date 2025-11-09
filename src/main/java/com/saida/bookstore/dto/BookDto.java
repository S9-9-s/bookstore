package com.saida.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookDto(
        UUID publicId,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        Integer publicationYear,
        LocalDateTime createdAt
) {
}