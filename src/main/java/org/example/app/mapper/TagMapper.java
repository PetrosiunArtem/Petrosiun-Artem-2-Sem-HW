package org.example.app.mapper;

import lombok.experimental.UtilityClass;
import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;

@UtilityClass
public class TagMapper {
  public TagDto toDto(Tag tag) {
    String name = tag.getName();
    Long id = tag.getId();
    return new TagDto(id, name);
  }
}
