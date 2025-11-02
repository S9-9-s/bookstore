package com.saida.bookstore.api.response;

import java.math.BigDecimal;

public record BookResponse(
        String name,
        BigDecimal price
) {
}
