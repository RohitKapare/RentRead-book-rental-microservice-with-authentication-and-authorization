package com.crio.rent_read.repository;

import com.crio.rent_read.entity.Rental;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

  List<Rental> findByUserIdAndReturnDateIsNull(Long userId);

  long countByUserIdAndReturnDateIsNull(Long userId);
}