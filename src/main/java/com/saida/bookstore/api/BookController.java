package com.saida.bookstore.api;

import com.saida.bookstore.api.request.BookRequest;
import com.saida.bookstore.api.response.BookResponse;
import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.mapper.BookMapper;
import com.saida.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Book Controller", description = "API для работы с книгами")
@Schema(description = "book")
@RestController
@RequestMapping(path = "/book")
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;
    private final BookService bookService;

    @Operation(summary = "Найти книгу по ID", description = "Возвращает книгу по её идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение книги!"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена:(")
    })
    @GetMapping("/{publicId}")
    ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "ID книги", required = true, example = "1")
            @PathVariable UUID publicId
    ) {
        BookDto dto = bookService.getBookById(publicId);
        return ResponseEntity.ok().body(bookMapper.toResponse(dto));
    }


    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка книг!")
    })
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
        return ResponseEntity.ok(books);
    }


    @Operation(summary = "Создать книгу", description = "Создание новой книги в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Книга успешно создана!"),
            @ApiResponse(responseCode = "400", description = "Неверные данные книги:(")
    })
    @PostMapping
    public ResponseEntity<BookResponse> saveBook(@RequestBody BookRequest request) {
        BookDto bookDto = bookService.saveBook(bookMapper.toDto(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookMapper.toResponse(bookDto));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable UUID publicId,
            @RequestBody BookRequest request) {
        BookDto updatedBook = bookService.updateBook(publicId, bookMapper.toDto(request));
        return ResponseEntity.ok(bookMapper.toResponse(updatedBook));
    }


    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("publicId") UUID publicId) {
        bookService.deleteBookById(publicId);
        return ResponseEntity.noContent().build();
    }

}