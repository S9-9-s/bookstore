package com.saida.bookstore.validator.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.validator.BookValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookAuthorValidator implements BookValidator {

    @Override
    public List<String> validate(BookDto bookDto) {
        List<String> errors = new ArrayList<>();

        if (bookDto.author() == null || bookDto.author().trim().isEmpty()) {
            errors.add("Author must not be empty");
        } else if (bookDto.author().length() > 255) {
            errors.add("Author must not exceed 255 characters");
        }

        return errors;
    }
}