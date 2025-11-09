package com.saida.bookstore.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saida.bookstore.api.request.BookRequest;
import com.saida.bookstore.api.response.BookResponse;
import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.mapper.BookMapper;
import com.saida.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookController bookController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UUID PUBLIC_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final String ISBN = "978-3-16-148410-0";
    private final String TITLE = "Test Book";
    private final String AUTHOR = "Test Author";
    private final BigDecimal PRICE = new BigDecimal("29.99");
    private final int PUBLICATION_YEAR = 2023;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        // Given
        BookDto bookDto = createBookDto();
        BookResponse bookResponse = createBookResponse();

        when(bookService.getBookById(PUBLIC_ID)).thenReturn(bookDto);
        when(bookMapper.toResponse(bookDto)).thenReturn(bookResponse);

        // When & Then
        mockMvc.perform(get("/book/{publicId}", PUBLIC_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.publicId").value(PUBLIC_ID.toString()))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.author").value(AUTHOR))
                .andExpect(jsonPath("$.isbn").value(ISBN))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.publicationYear").value(PUBLICATION_YEAR));

        verify(bookService).getBookById(PUBLIC_ID);
        verify(bookMapper).toResponse(bookDto);
    }

    @Test
    void getAllBooks_WhenBooksExist_ShouldReturnBookList() throws Exception {
        // Given
        BookDto bookDto1 = createBookDto();
        BookDto bookDto2 = new BookDto(UUID.randomUUID(), "Another Book", "Another Author",
                "978-1-23-456789-0", new BigDecimal("39.99"), 2024, LocalDateTime.now());

        BookResponse response1 = createBookResponse();
        BookResponse response2 = new BookResponse(bookDto2.publicId(), bookDto2.title(), bookDto2.author(),
                bookDto2.isbn(), bookDto2.price(), bookDto2.publicationYear(), bookDto2.createdAt());

        when(bookService.getAllBooks()).thenReturn(List.of(bookDto1, bookDto2));
        when(bookMapper.toResponse(bookDto1)).thenReturn(response1);
        when(bookMapper.toResponse(bookDto2)).thenReturn(response2);

        // When & Then
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(TITLE))
                .andExpect(jsonPath("$[1].title").value("Another Book"));

        verify(bookService).getAllBooks();
        verify(bookMapper, times(2)).toResponse(any(BookDto.class));
    }

    @Test
    void saveBook_WhenValidRequest_ShouldCreateBook() throws Exception {
        // Given
        BookRequest bookRequest = createBookRequest();
        BookDto bookDto = createBookDto();
        BookResponse bookResponse = createBookResponse();

        when(bookMapper.toDto(bookRequest)).thenReturn(bookDto);
        when(bookService.saveBook(bookDto)).thenReturn(bookDto);
        when(bookMapper.toResponse(bookDto)).thenReturn(bookResponse);

        // When & Then
        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.publicId").value(PUBLIC_ID.toString()))
                .andExpect(jsonPath("$.title").value(TITLE));

        verify(bookMapper).toDto(bookRequest);
        verify(bookService).saveBook(bookDto);
        verify(bookMapper).toResponse(bookDto);
    }

    @Test
    void updateBook_WhenValidRequest_ShouldUpdateBook() throws Exception {
        // Given
        BookRequest bookRequest = createBookRequest();
        BookDto bookDto = createBookDto();
        BookResponse bookResponse = createBookResponse();

        when(bookMapper.toDto(bookRequest)).thenReturn(bookDto);
        when(bookService.updateBook(eq(PUBLIC_ID), eq(bookDto))).thenReturn(bookDto);
        when(bookMapper.toResponse(bookDto)).thenReturn(bookResponse);

        // When & Then
        mockMvc.perform(put("/book/{publicId}", PUBLIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.publicId").value(PUBLIC_ID.toString()))
                .andExpect(jsonPath("$.title").value(TITLE));

        verify(bookMapper).toDto(bookRequest);
        verify(bookService).updateBook(PUBLIC_ID, bookDto);
        verify(bookMapper).toResponse(bookDto);
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(bookService).deleteBookById(PUBLIC_ID);

        // When & Then
        mockMvc.perform(delete("/book/{publicId}", PUBLIC_ID))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBookById(PUBLIC_ID);
    }

    private BookRequest createBookRequest() {
        return new BookRequest(TITLE, AUTHOR, ISBN, PRICE, PUBLICATION_YEAR);
    }

    private BookDto createBookDto() {
        return new BookDto(PUBLIC_ID, TITLE, AUTHOR, ISBN, PRICE, PUBLICATION_YEAR, LocalDateTime.now());
    }

    private BookResponse createBookResponse() {
        return new BookResponse(PUBLIC_ID, TITLE, AUTHOR, ISBN, PRICE, PUBLICATION_YEAR, LocalDateTime.now());
    }
}