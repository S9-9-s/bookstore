package com.saida.bookstore.service.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.entity.BookEntity;
import com.saida.bookstore.exception.BookAlreadyExistsException;
import com.saida.bookstore.exception.BookNotFoundException;
import com.saida.bookstore.exception.InvalidBookDataException;
import com.saida.bookstore.mapper.BookMapper;
import com.saida.bookstore.repository.BookRepository;
import com.saida.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final int MIN_PUBLICATION_YEAR = 1500;

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto getBookById(UUID publicId) {
        log.debug("Finding book by publicId: {}", publicId);

        BookEntity entity = bookRepository.findById(publicId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with publicId: " + publicId));

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
        log.debug("Creating new book with title: {}", bookDto.title());

        validateBookData(bookDto);

        if (bookRepository.existsByIsbn(bookDto.isbn())) {
            throw new BookAlreadyExistsException("Book with ISBN '" + bookDto.isbn() + "' already exists");
        }

        BookEntity entity = bookMapper.toEntity(bookDto);
        BookEntity savedEntity = bookRepository.save(entity);

        log.info("Successfully created book with publicId: {}", savedEntity.getPublicId());
        return bookMapper.toDto(savedEntity);
    }

    @Override
    public BookDto updateBook(UUID publicId, BookDto bookDto) {
        log.debug("Updating book with publicId: {}", publicId);

        validateBookData(bookDto);

        BookEntity existingEntity = bookRepository.findById(publicId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with publicId: " + publicId));

        // Проверяем, не используется ли ISBN другой книгой
        if (!existingEntity.getIsbn().equals(bookDto.isbn()) &&
                bookRepository.existsByIsbn(bookDto.isbn())) {
            throw new BookAlreadyExistsException("ISBN '" + bookDto.isbn() + "' is already used by another book");
        }

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

        log.info("Successfully updated book with publicId: {}", publicId);
        return bookMapper.toDto(updatedEntity);
    }

    @Override
    public void deleteBookById(UUID publicId) {
        log.debug("Deleting book with publicId: {}", publicId);

        // Проверяем существование книги перед удалением
        if (bookRepository.findById(publicId).isEmpty()) {
            throw new BookNotFoundException("Book not found with publicId: " + publicId);
        }

        bookRepository.deleteById(publicId);
        log.info("Successfully deleted book with publicId: {}", publicId);
    }

    private void validateBookData(BookDto bookDto) {
        if (bookDto.price() == null || bookDto.price().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidBookDataException("Price must be greater than 0");
        }

        int currentYear = Year.now().getValue();
        if (bookDto.publicationYear() == null ||
                bookDto.publicationYear() < MIN_PUBLICATION_YEAR ||
                bookDto.publicationYear() > currentYear) {
            throw new InvalidBookDataException(String.format("Publication year must be between %s and %s",
                    MIN_PUBLICATION_YEAR, currentYear));
        }

        if (bookDto.title() == null || bookDto.title().trim().isEmpty()) {
            throw new InvalidBookDataException("Title must not be empty");
        }

        if (bookDto.author() == null || bookDto.author().trim().isEmpty()) {
            throw new InvalidBookDataException("Author must not be empty");
        }

        if (bookDto.isbn() == null || bookDto.isbn().trim().isEmpty()) {
            throw new InvalidBookDataException("ISBN must not be empty");
        }
    }
}