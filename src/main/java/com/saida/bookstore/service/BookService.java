package com.saida.bookstore.service;

import com.saida.bookstore.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookDto> findBookById(Long id);

    List<BookDto> findAllBooks();

    BookDto saveBook(BookDto bookDto);

    void deleteBookById(Long id);

}
