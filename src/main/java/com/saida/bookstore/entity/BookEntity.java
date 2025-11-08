package com.saida.bookstore.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookEntity {
    private Long id;
    private UUID publicId;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer publicationYear;
    private LocalDateTime createdAt;
}
