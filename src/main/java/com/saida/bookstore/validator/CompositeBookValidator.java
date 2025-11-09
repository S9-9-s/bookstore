package com.saida.bookstore.validator;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeBookValidator {

    private final List<BookValidator> validators;

    public void validate(BookDto bookDto) {
        List<String> errors = validators.stream()
                .flatMap(validator -> validator.validate(bookDto).stream())
                .toList();

        if (!errors.isEmpty()) {
            throw new ValidationException("Book data validation failed", errors);
        }
    }
}