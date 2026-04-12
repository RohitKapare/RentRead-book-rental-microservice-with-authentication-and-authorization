package com.crio.rent_read.mapper;

import com.crio.rent_read.dto.response.RentalResponse;
import com.crio.rent_read.entity.Rental;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface RentalMapper {

  RentalResponse toRentalResponse(Rental rental);

}