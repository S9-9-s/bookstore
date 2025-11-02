package com.saida.bookstore.mapper;

import com.saida.bookstore.api.response.BookResponse;
import com.saida.bookstore.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    BookResponse toResponse(BookDto bookDto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    BookDto toDto(BookResponse bookResponse);
}
