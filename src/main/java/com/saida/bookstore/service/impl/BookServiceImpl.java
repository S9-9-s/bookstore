package com.saida.bookstore.service.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.entity.BookEntity;
import com.saida.bookstore.mapper.BookMapper;
import com.saida.bookstore.repository.BookRepository;
import com.saida.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto getBookById(UUID publicId) {
        BookEntity entity = bookRepository.findById(publicId)
                .orElseThrow(() -> new RuntimeException("Book not found")); // Замените на ваше исключение
        return bookMapper.toDto(entity);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto saveBook(BookDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.isbn())) {
            throw new RuntimeException("ISBN already exists"); // Замените на ваше исключение
        }

        BookEntity entity = bookMapper.toEntity(bookDto);
        BookEntity savedEntity = bookRepository.save(entity);
        return bookMapper.toDto(savedEntity);
    }

    @Override
    public BookDto updateBook(UUID publicId, BookDto bookDto) {
        // Проверяем существование книги
        BookEntity existingEntity = bookRepository.findById(publicId)
                .orElseThrow(() -> new RuntimeException("Book not found with publicId: " + publicId));

        BookEntity entity = BookEntity.builder()
                .id(existingEntity.getId())
                .publicId(publicId)
                .title(bookDto.title())
                .author(bookDto.author())
                .isbn(bookDto.isbn())
                .price(bookDto.price())
                .publicationYear(bookDto.publicationYear())
                .createdAt(existingEntity.getCreatedAt())
                .build();

        BookEntity updatedEntity = bookRepository.save(entity);
        return bookMapper.toDto(updatedEntity);
    }

    @Override
    public void deleteBookById(UUID publicId) {
        bookRepository.deleteById(publicId);
    }
}