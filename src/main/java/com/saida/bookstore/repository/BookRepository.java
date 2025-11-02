package com.saida.bookstore.repository;

import com.saida.bookstore.dto.BookDto;

import java.util.Optional;

public interface BookRepository {

    Optional<BookDto> findById(Long id);
}
