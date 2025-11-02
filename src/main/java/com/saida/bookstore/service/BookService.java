package com.saida.bookstore.service;

import com.saida.bookstore.dto.BookDto;

import java.util.Optional;

public interface BookService {

    Optional<BookDto> findBookById(Long id);
}
