package com.crio.rent_read.entity;

import com.crio.rent_read.entity.enums.AvailabilityStatus;
import com.crio.rent_read.entity.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Genre genre;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AvailabilityStatus availabilityStatus;
}