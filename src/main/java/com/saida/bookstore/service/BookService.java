package com.saida.bookstore.service;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.exception.BookAlreadyExistsException;
import com.saida.bookstore.exception.BookNotFoundException;
import com.saida.bookstore.exception.ValidationException;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления книгами.
 * Предоставляет операции для поиска, создания, обновления и удаления книг.
 */
public interface BookService {

    /**
     * Находит книгу по публичному идентификатору.
     *
     * @param publicId уникальный публичный идентификатор книги
     * @return DTO книги
     * @throws BookNotFoundException если книга с указанным publicId не найдена
     */
    BookDto getBookById(UUID publicId);

    /**
     * Возвращает все книги из системы.
     *
     * @return список всех DTO книг, пустой список если книги не найдены
     */
    List<BookDto> getAllBooks();

    /**
     * Создает новую книгу.
     *
     * @param bookDto данные книги для создания
     * @return созданная DTO книги с сгенерированным publicId
     * @throws BookAlreadyExistsException если книга с таким ISBN уже существует
     * @throws ValidationException        если данные книги невалидны
     */
    BookDto saveBook(BookDto bookDto);

    /**
     * Обновляет существующую книгу.
     *
     * @param publicId публичный идентификатор обновляемой книги
     * @param bookDto  новые данные книги
     * @return обновленная DTO книги
     * @throws BookNotFoundException      если книга с указанным publicId не найдена
     * @throws BookAlreadyExistsException если новый ISBN уже используется другой книгой
     * @throws ValidationException        если новые данные книги невалидны
     */
    BookDto updateBook(UUID publicId, BookDto bookDto);

    /**
     * Удаляет книгу по публичному идентификатору.
     *
     * @param publicId публичный идентификатор удаляемой книги
     * @throws BookNotFoundException если книга с указанным publicId не найдена
     */
    void deleteBookById(UUID publicId);
}