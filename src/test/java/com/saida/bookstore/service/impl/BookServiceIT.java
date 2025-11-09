package com.saida.bookstore.service.impl;

import com.saida.bookstore.BaseIntegrationTest;
import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceIT extends BaseIntegrationTest {

    @Autowired
    private BookService bookService;

    private BookDto testBookDto;

    @BeforeEach
    void setUp() {
        testBookDto = new BookDto(
                null,
                "Test Book Title",
                "Test Author",
                "1234567890",
                new BigDecimal("29.99"),
                2023,
                null
        );
    }

    @Test
    @DisplayName("Успешное создание книги и проверка всех полей")
    void saveBook_WhenValidData_ShouldSaveAndReturnBook() {
        // When
        BookDto savedBook = bookService.saveBook(testBookDto);

        // Then
        assertNotNull(savedBook);
        assertNotNull(savedBook.publicId());
        assertThat(savedBook.publicId()).isNotNull();

        // Проверяем все поля
        assertEquals(testBookDto.title(), savedBook.title());
        assertEquals(testBookDto.author(), savedBook.author());
        assertEquals(testBookDto.isbn(), savedBook.isbn());
        assertEquals(0, testBookDto.price().compareTo(savedBook.price()));
        assertEquals(testBookDto.publicationYear(), savedBook.publicationYear());

        // Проверяем, что createdAt установлен
        assertNotNull(savedBook.createdAt());

        // Дополнительная проверка через получение книги по ID
        BookDto retrievedBook = bookService.getBookById(savedBook.publicId());
        assertNotNull(retrievedBook);
        assertEquals(savedBook.publicId(), retrievedBook.publicId());
        assertEquals(savedBook.title(), retrievedBook.title());
    }
}