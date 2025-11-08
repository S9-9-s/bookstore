package com.saida.bookstore.service.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.repository.BookRepository;
import com.saida.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Optional<BookDto> findBookById(Long id) {
        return bookRepository.findById(id);
    }


    @Override
    public List<BookDto> findAllBooks() {
        return bookRepository.findAllBooks();
    }

    @Override
    public BookDto saveBook(BookDto bookDto) {
        return bookRepository.saveBook(bookDto);
    }


    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteBookById(id);
    }

}
