package com.saida.bookstore.api;

import com.saida.bookstore.api.response.BookResponse;
import com.saida.bookstore.mapper.BookMapper;
import com.saida.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Controller", description = "API для работы с книгами")
@Schema(description = "test")
@RestController
@RequestMapping(path = "/book")
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;
    private final BookService bookService;

    @Operation(summary = "Найти книгу по ID", description = "Возвращает книгу по её идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение книги"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    @GetMapping("/{id}")
    ResponseEntity<BookResponse> findBookById(
            @Parameter(description = "ID книги", required = true, example = "1")
            @PathVariable Long id
    ) {
        return bookService.findBookById(id)
                .map(bookMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}