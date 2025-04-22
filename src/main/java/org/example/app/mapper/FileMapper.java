package org.example.app.mapper;

import lombok.experimental.UtilityClass;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;

@UtilityClass
public class FileMapper {
  public static FileDto toDto(File file) {
    Long id = file.getId();
    String name = file.getName();
    int capacity = file.getCapacity();

    return new FileDto(id, name, capacity);
  }
}
