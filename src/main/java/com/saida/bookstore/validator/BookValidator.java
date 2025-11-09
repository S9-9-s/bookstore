package com.saida.bookstore.validator;

import com.saida.bookstore.dto.BookDto;

import java.util.List;

public interface BookValidator {
    List<String> validate(BookDto bookDto);
}