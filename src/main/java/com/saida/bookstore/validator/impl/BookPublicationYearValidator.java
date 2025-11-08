package com.saida.bookstore.validator.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.validator.BookValidator;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookPublicationYearValidator implements BookValidator {

    private static final int MIN_PUBLICATION_YEAR = 1500;

    @Override
    public List<String> validate(BookDto bookDto) {
        List<String> errors = new ArrayList<>();

        int currentYear = Year.now().getValue();

        if (bookDto.publicationYear() == null) {
            errors.add("Publication year must not be null");
        } else if (bookDto.publicationYear() < MIN_PUBLICATION_YEAR) {
            errors.add(String.format("Publication year must be at least %s", MIN_PUBLICATION_YEAR));
        } else if (bookDto.publicationYear() > currentYear) {
            errors.add(String.format("Publication year must not exceed current year %s", currentYear));
        }

        return errors;
    }
}