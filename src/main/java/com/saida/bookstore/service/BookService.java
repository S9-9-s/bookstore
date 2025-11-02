package com.saida.bookstore.service;

import com.saida.bookstore.dto.BookDto;

public interface BookService {

    BookDto findBookById(Long id);
}
