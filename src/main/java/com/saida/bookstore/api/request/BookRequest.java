package com.saida.bookstore.api.request;

import java.math.BigDecimal;

public record BookRequest(
        String title,
        String author,
        String isbn,
        BigDecimal price,
        Integer publicationYear
) {
}
