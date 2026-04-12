package com.crio.rent_read.mapper;

import com.crio.rent_read.dto.response.RentalResponse;
import com.crio.rent_read.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface RentalMapper {

  RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);

  RentalResponse toRentalResponse(Rental rental);
}