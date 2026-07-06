package com.crio.rent_read.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum Genre {
  FICTION,
  NON_FICTION,
  HISTORY,
  FANTASY,
  MYSTERY,
  ROMANCE,
  THRILLER,
  OTHER;

  @JsonCreator
  public static Genre fromString(String value) {
    return Arrays.stream(Genre.values())
        .filter(genre -> genre.name().equalsIgnoreCase(value.trim().replace("-", "_")))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown genre: " + value));
  }

}