package com.crio.rent_read.mapper;

import com.crio.rent_read.dto.request.BookRequest;
import com.crio.rent_read.dto.request.BookUpdateRequest;
import com.crio.rent_read.dto.response.BookResponse;
import com.crio.rent_read.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

  BookResponse toBookResponse(Book book);

  @Mapping(target = "id", ignore = true)
  Book toBook(BookRequest bookRequest);

  void updateBookFromDto(BookUpdateRequest dto, @MappingTarget Book book);
}
