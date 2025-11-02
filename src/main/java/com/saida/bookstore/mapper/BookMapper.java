package com.saida.bookstore.mapper;

import com.saida.bookstore.api.response.BookResponse;
import com.saida.bookstore.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "publicationYear", target = "publicationYear")
    @Mapping(source = "createdAt", target = "createdAt")
    BookResponse toResponse(BookDto bookDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "publicationYear", target = "publicationYear")
    @Mapping(source = "createdAt", target = "createdAt")
    BookDto toDto(BookResponse bookResponse);
}
