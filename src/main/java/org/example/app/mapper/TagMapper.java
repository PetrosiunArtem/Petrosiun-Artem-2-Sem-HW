package org.example.app.mapper;


import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagDto toDto(Tag tag) {
        String name = tag.getName();
        Long id = tag.getId();
        return new TagDto(id, name);
    }

    public Tag toTag(TagDto tagDto) {
        return new Tag(tagDto.name());
    }
}