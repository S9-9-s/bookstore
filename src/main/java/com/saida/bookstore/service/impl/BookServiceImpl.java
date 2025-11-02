package com.saida.bookstore.service.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookServiceImpl implements BookService {

    @Override
    public BookDto findBookById(Long id) {
        return new BookDto("Война и мир", new BigDecimal("100.00"));
    }
}
