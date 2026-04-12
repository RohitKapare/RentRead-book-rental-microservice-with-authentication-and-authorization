package com.crio.rent_read.mapper;

import com.crio.rent_read.dto.request.BookRequest;
import com.crio.rent_read.dto.request.BookUpdateRequest;
import com.crio.rent_read.dto.response.BookResponse;
import com.crio.rent_read.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {

  BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

  BookResponse toBookResponse(Book book);

  Book toBook(BookRequest bookRequest);

  void updateBookFromDto(BookUpdateRequest dto, @MappingTarget Book book);
}
