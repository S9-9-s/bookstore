package com.saida.bookstore.validator.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.util.IsbnUtils;
import com.saida.bookstore.validator.BookValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookIsbnValidator implements BookValidator {

    private static final int MAX_ISBN_LEN = 20;

    @Override
    public List<String> validate(BookDto bookDto) {
        List<String> errors = new ArrayList<>();

        if (bookDto.isbn() == null || bookDto.isbn().trim().isEmpty()) {
            errors.add("ISBN must not be empty");
        } else {
            String normalizedIsbn = IsbnUtils.normalize(bookDto.isbn());

            if (normalizedIsbn.length() > MAX_ISBN_LEN) {
                errors.add(String.format("ISBN must not exceed %d characters", MAX_ISBN_LEN));
            } else if (!IsbnUtils.isValidFormat(normalizedIsbn)) {
                errors.add("Invalid ISBN format. Examples: '1234567890' or '9781234567890'");
            }
        }

        return errors;
    }
}