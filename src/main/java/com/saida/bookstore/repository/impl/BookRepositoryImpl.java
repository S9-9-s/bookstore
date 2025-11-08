package com.saida.bookstore.repository.impl;

import com.saida.bookstore.dto.BookDto;
import com.saida.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private static final String FIND_BY_ID_SELECT = """
            select * from book where id = :id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<BookDto> findById(Long id) {
        try {
            BookDto book = jdbcTemplate.queryForObject(
                    FIND_BY_ID_SELECT,
                    Map.of("id", id),
                    bookRowMapper
            );
            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Book not found with id: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<BookDto> findAllBooks() {
        // todo: implement me
        return List.of();
    }

    @Override
    public BookDto saveBook(BookDto bookDto) {
        // todo: implement me
        return null;
    }


    @Override
    public void deleteBookById(Long id) {
        // todo: implement me
    }

    private final RowMapper<BookDto> bookRowMapper = (rs, rowNum) ->
            new BookDto(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getBigDecimal("price"),
                    rs.getInt("publication_year"),
                    rs.getTimestamp("created_at") != null ?
                            rs.getTimestamp("created_at").toLocalDateTime() : null
            );
}
