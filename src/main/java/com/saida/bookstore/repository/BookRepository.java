package com.saida.bookstore.repository;

import com.saida.bookstore.entity.BookEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    Optional<BookEntity> findById(UUID publicId);

    List<BookEntity> findAll();

    BookEntity save(BookEntity book);

    void deleteById(UUID publicId);

    boolean existsByIsbn(String isbn);
}