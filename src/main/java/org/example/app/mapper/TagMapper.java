package org.example.app.mapper;


import org.example.app.dto.TagDto;
import org.example.app.entity.File;
import org.example.app.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TagMapper {
    public TagDto toDto(Tag tag) {
        String name = tag.getName();
        Long id = tag.getId();
        //Set<File> files = tag.getFiles();

        return new TagDto(id, name);
    }

    public Tag toTag(TagDto tagDto) {
        return new Tag(tagDto.getName(), tagDto.getFiles());
    }
}