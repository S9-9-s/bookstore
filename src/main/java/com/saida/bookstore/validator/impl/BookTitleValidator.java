package com.saida.bookstore.validator.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.validator.BookValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookTitleValidator implements BookValidator {

    private static final int MAX_TITLE_LENGTH = 255;

    @Override
    public List<String> validate(BookDto bookDto) {
        List<String> errors = new ArrayList<>();

        if (bookDto.title() == null || bookDto.title().trim().isEmpty()) {
            errors.add("Title must not be empty");
        } else {
            if (bookDto.title().length() > MAX_TITLE_LENGTH) {
                errors.add(String.format("Title must not exceed %d characters", MAX_TITLE_LENGTH));
            }
        }

        return errors;
    }
}