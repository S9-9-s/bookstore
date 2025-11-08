package com.saida.bookstore.repository;

import com.saida.bookstore.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<BookDto> findById(Long id);

    List<BookDto> findAllBooks();

    BookDto saveBook(BookDto bookDto);

    void deleteBookById(Long id);
}
