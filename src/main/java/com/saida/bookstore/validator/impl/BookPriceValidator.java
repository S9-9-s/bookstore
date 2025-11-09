package com.saida.bookstore.validator.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.validator.BookValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookPriceValidator implements BookValidator {

    private static final int MAX_SCALE_SIZE = 2;

    @Override
    public List<String> validate(BookDto bookDto) {
        List<String> errors = new ArrayList<>();

        if (bookDto.price() == null) {
            errors.add("Price must not be null");
        } else if (bookDto.price().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Price must be greater than 0");
        } else if (bookDto.price().scale() > MAX_SCALE_SIZE) {
            errors.add("Price must have at most 2 decimal places");
        }

        return errors;
    }
}