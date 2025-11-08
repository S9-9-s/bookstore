package com.saida.bookstore.repository.impl;

import com.saida.bookstore.entity.BookEntity;
import com.saida.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private static final String FIND_BY_PUBLIC_ID_SELECT = """
            SELECT id, public_id, title, author, isbn, price, publication_year, created_at 
            FROM book WHERE public_id = :publicId
            """;

    private static final String FIND_ALL_SELECT = """
            SELECT id, public_id, title, author, isbn, price, publication_year, created_at 
            FROM book ORDER BY created_at DESC
            """;

    private static final String INSERT_BOOK = """
            INSERT INTO book (public_id, title, author, isbn, price, publication_year, created_at)
            VALUES (:publicId, :title, :author, :isbn, :price, :publicationYear, :createdAt)
            RETURNING id, public_id, title, author, isbn, price, publication_year, created_at
            """;

    private static final String UPDATE_BOOK = """
            UPDATE book 
            SET title = :title, author = :author, isbn = :isbn, 
                price = :price, publication_year = :publicationYear
            WHERE public_id = :publicId
            RETURNING id, public_id, title, author, isbn, price, publication_year, created_at
            """;

    private static final String DELETE_BOOK = """
            DELETE FROM book WHERE public_id = :publicId
            """;

    private static final String EXISTS_BY_ISBN = """
            SELECT COUNT(*) FROM book WHERE isbn = :isbn
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<BookEntity> findById(UUID publicId) {
        try {
            BookEntity book = jdbcTemplate.queryForObject(
                    FIND_BY_PUBLIC_ID_SELECT,
                    Map.of("publicId", publicId.toString()),
                    bookRowMapper
            );
            return Optional.of(book);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Book not found with public id: {}", publicId);
            return Optional.empty();
        }
    }

    @Override
    public List<BookEntity> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_SELECT, bookRowMapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while fetching all books", e);
        }
    }

    @Override
    public BookEntity save(BookEntity book) {
        if (book.getId() == null) {
            return insert(book);
        } else {
            return update(book);
        }
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    EXISTS_BY_ISBN,
                    Map.of("isbn", isbn),
                    Integer.class
            );
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error checking ISBN existence", e);
        }
    }

    private BookEntity insert(BookEntity book) {
        UUID publicId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        Map<String, Object> params = Map.of(
                "publicId", publicId.toString(),
                "title", book.getTitle(),
                "author", book.getAuthor(),
                "isbn", book.getIsbn(),
                "price", book.getPrice(),
                "publicationYear", book.getPublicationYear(),
                "createdAt", Timestamp.valueOf(createdAt)
        );

        try {
            return jdbcTemplate.queryForObject(INSERT_BOOK, params, bookRowMapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save book with title: " + book.getTitle(), e);
        }
    }

    private BookEntity update(BookEntity book) {
        Map<String, Object> params = Map.of(
                "publicId", book.getPublicId().toString(),
                "title", book.getTitle(),
                "author", book.getAuthor(),
                "isbn", book.getIsbn(),
                "price", book.getPrice(),
                "publicationYear", book.getPublicationYear()
        );

        try {
            return jdbcTemplate.queryForObject(UPDATE_BOOK, params, bookRowMapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update book with public id: " + book.getPublicId(), e);
        }
    }

    @Override
    public void deleteById(UUID publicId) {
        try {
            int affectedRows = jdbcTemplate.update(DELETE_BOOK, Map.of("publicId", publicId.toString()));
            if (affectedRows == 0) {
                log.warn("No book found with public id: {} for deletion", publicId);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete book with public id: " + publicId, e);
        }
    }

    private final RowMapper<BookEntity> bookRowMapper = (rs, rowNum) ->
            BookEntity.builder()
                    .id(rs.getLong("id"))
                    .publicId(UUID.fromString(rs.getString("public_id")))
                    .title(rs.getString("title"))
                    .author(rs.getString("author"))
                    .isbn(rs.getString("isbn"))
                    .price(rs.getBigDecimal("price"))
                    .publicationYear(rs.getInt("publication_year"))
                    .createdAt(rs.getTimestamp("created_at") != null ?
                            rs.getTimestamp("created_at").toLocalDateTime() : null)
                    .build();

}