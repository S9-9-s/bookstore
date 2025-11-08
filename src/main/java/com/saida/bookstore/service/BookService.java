package com.saida.bookstore.service;

import com.saida.bookstore.dto.BookDto;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookDto getBookById(UUID publicId);

    List<BookDto> getAllBooks();

    BookDto saveBook(BookDto bookDto);

    BookDto updateBook(UUID publicId, BookDto bookDto);

    void deleteBookById(UUID publicId);

}
