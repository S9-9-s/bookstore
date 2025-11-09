package com.saida.bookstore.service.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.entity.BookEntity;
import com.saida.bookstore.exception.BookAlreadyExistsException;
import com.saida.bookstore.exception.BookNotFoundException;
import com.saida.bookstore.mapper.BookMapper;
import com.saida.bookstore.repository.BookRepository;
import com.saida.bookstore.validator.CompositeBookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CompositeBookValidator compositeBookValidator;

    @InjectMocks
    private BookServiceImpl bookService;

    private final UUID PUBLIC_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final String ISBN = "978-3-16-148410-0";
    private final String TITLE = "Test Book";
    private final String AUTHOR = "Test Author";
    private final BigDecimal PRICE = new BigDecimal("29.99");
    private final int PUBLICATION_YEAR = 2023;

    @Test
    void getBookById_WhenBookExists_ShouldReturnBookDto() {
        // Given
        BookEntity bookEntity = createBookEntity();
        BookDto expectedBookDto = createBookDto();

        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toDto(bookEntity)).thenReturn(expectedBookDto);

        // When
        BookDto result = bookService.getBookById(PUBLIC_ID);

        // Then
        assertThat(result).isEqualTo(expectedBookDto);
        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookMapper).toDto(bookEntity);
    }

    @Test
    void getBookById_WhenBookNotExists_ShouldThrowException() {
        // Given
        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.getBookById(PUBLIC_ID))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found with publicId: " + PUBLIC_ID);

        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookMapper, never()).toDto(any(BookEntity.class));
    }

    @Test
    void getAllBooks_WhenBooksExist_ShouldReturnBookDtoList() {
        // Given
        BookEntity bookEntity1 = createBookEntity();
        BookEntity bookEntity2 = createBookEntity();
        bookEntity2.setPublicId(UUID.randomUUID());
        bookEntity2.setTitle("Another Book");

        BookDto bookDto1 = createBookDto();
        BookDto bookDto2 = createBookDto();
        bookDto2 = new BookDto(bookDto2.publicId(), "Another Book", bookDto2.author(),
                bookDto2.isbn(), bookDto2.price(), bookDto2.publicationYear(), now());

        when(bookRepository.findAll()).thenReturn(List.of(bookEntity1, bookEntity2));
        when(bookMapper.toDto(bookEntity1)).thenReturn(bookDto1);
        when(bookMapper.toDto(bookEntity2)).thenReturn(bookDto2);

        // When
        List<BookDto> result = bookService.getAllBooks();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(bookDto1, bookDto2);
        verify(bookRepository).findAll();
        verify(bookMapper, times(2)).toDto(any(BookEntity.class));
    }

    @Test
    void getAllBooks_WhenNoBooks_ShouldReturnEmptyList() {
        // Given
        when(bookRepository.findAll()).thenReturn(List.of());

        // When
        List<BookDto> result = bookService.getAllBooks();

        // Then
        assertThat(result).isEmpty();
        verify(bookRepository).findAll();
        verify(bookMapper, never()).toDto(any(BookEntity.class));
    }

    @Test
    void saveBook_WhenValidBook_ShouldSaveAndReturnBookDto() {
        // Given
        BookDto inputBookDto = createBookDtoWithoutId();
        BookEntity savedEntity = createBookEntity();
        BookDto expectedBookDto = createBookDto();

        doNothing().when(compositeBookValidator).validate(inputBookDto);
        when(bookRepository.existsByIsbn(ISBN)).thenReturn(false);
        when(bookMapper.toEntity(inputBookDto)).thenReturn(savedEntity);
        when(bookRepository.save(savedEntity)).thenReturn(savedEntity);
        when(bookMapper.toDto(savedEntity)).thenReturn(expectedBookDto);

        // When
        BookDto result = bookService.saveBook(inputBookDto);

        // Then
        assertThat(result).isEqualTo(expectedBookDto);
        verify(compositeBookValidator).validate(inputBookDto);
        verify(bookRepository).existsByIsbn(ISBN);
        verify(bookMapper).toEntity(inputBookDto);
        verify(bookRepository).save(savedEntity);
        verify(bookMapper).toDto(savedEntity);
    }

    @Test
    void saveBook_WhenIsbnAlreadyExists_ShouldThrowException() {
        // Given
        BookDto inputBookDto = createBookDtoWithoutId();

        doNothing().when(compositeBookValidator).validate(inputBookDto);
        when(bookRepository.existsByIsbn(ISBN)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> bookService.saveBook(inputBookDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("Book with ISBN '" + ISBN + "' already exists");

        verify(compositeBookValidator).validate(inputBookDto);
        verify(bookRepository).existsByIsbn(ISBN);
        verify(bookMapper, never()).toEntity(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_WhenValidUpdate_ShouldUpdateAndReturnBookDto() {
        // Given
        BookDto updateBookDto = new BookDto(null, "Updated Title", "Updated Author",
                ISBN, new BigDecimal("39.99"), 2024, now());
        BookEntity existingEntity = createBookEntity();
        // Устанавливаем другой ISBN у существующей entity, чтобы условие сработало
        existingEntity.setIsbn("old-isbn-123");

        BookEntity updatedEntity = createBookEntity();
        updatedEntity.setTitle("Updated Title");
        updatedEntity.setAuthor("Updated Author");
        updatedEntity.setPrice(new BigDecimal("39.99"));
        updatedEntity.setPublicationYear(2024);

        BookDto expectedBookDto = new BookDto(PUBLIC_ID, "Updated Title", "Updated Author",
                ISBN, new BigDecimal("39.99"), 2024, now());

        doNothing().when(compositeBookValidator).validate(updateBookDto);
        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.of(existingEntity));
        when(bookRepository.existsByIsbn(ISBN)).thenReturn(false);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(updatedEntity);
        when(bookMapper.toDto(updatedEntity)).thenReturn(expectedBookDto);

        // When
        BookDto result = bookService.updateBook(PUBLIC_ID, updateBookDto);

        // Then
        assertThat(result).isEqualTo(expectedBookDto);
        verify(compositeBookValidator).validate(updateBookDto);
        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookRepository).existsByIsbn(ISBN); // Теперь этот вызов произойдет
        verify(bookRepository).save(any(BookEntity.class));
        verify(bookMapper).toDto(updatedEntity);
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowException() {
        // Given
        BookDto updateBookDto = createBookDto();

        doNothing().when(compositeBookValidator).validate(updateBookDto);
        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.updateBook(PUBLIC_ID, updateBookDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found with publicId: " + PUBLIC_ID);

        verify(compositeBookValidator).validate(updateBookDto);
        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookRepository, never()).existsByIsbn(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_WhenIsbnChangedAndAlreadyExists_ShouldThrowException() {
        // Given
        String newIsbn = "978-1-23-456789-0";
        BookDto updateBookDto = new BookDto(null, TITLE, AUTHOR, newIsbn, PRICE, PUBLICATION_YEAR, now());
        BookEntity existingEntity = createBookEntity();

        doNothing().when(compositeBookValidator).validate(updateBookDto);
        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.of(existingEntity));
        when(bookRepository.existsByIsbn(newIsbn)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> bookService.updateBook(PUBLIC_ID, updateBookDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("ISBN '" + newIsbn + "' is already used by another book");

        verify(compositeBookValidator).validate(updateBookDto);
        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookRepository).existsByIsbn(newIsbn);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBookById_WhenBookExists_ShouldDeleteBook() {
        // Given
        BookEntity existingEntity = createBookEntity();
        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.of(existingEntity));
        doNothing().when(bookRepository).deleteById(PUBLIC_ID);

        // When
        bookService.deleteBookById(PUBLIC_ID);

        // Then
        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookRepository).deleteById(PUBLIC_ID);
    }

    @Test
    void deleteBookById_WhenBookNotExists_ShouldThrowException() {
        // Given
        when(bookRepository.findById(PUBLIC_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.deleteBookById(PUBLIC_ID))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found with publicId: " + PUBLIC_ID);

        verify(bookRepository).findById(PUBLIC_ID);
        verify(bookRepository, never()).deleteById(any());
    }

    private BookEntity createBookEntity() {
        return BookEntity.builder()
                .id(1L)
                .publicId(PUBLIC_ID)
                .title(TITLE)
                .author(AUTHOR)
                .isbn(ISBN)
                .price(PRICE)
                .publicationYear(PUBLICATION_YEAR)
                .createdAt(now())
                .build();
    }

    private BookDto createBookDto() {
        return new BookDto(PUBLIC_ID, TITLE, AUTHOR, ISBN, PRICE, PUBLICATION_YEAR, now());
    }

    private BookDto createBookDtoWithoutId() {
        return new BookDto(null, TITLE, AUTHOR, ISBN, PRICE, PUBLICATION_YEAR, now());
    }
}