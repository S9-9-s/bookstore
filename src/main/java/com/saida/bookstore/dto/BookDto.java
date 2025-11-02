package com.saida.bookstore.dto;

import java.math.BigDecimal;

public record BookDto (
        String name,
        BigDecimal price
) {
}
