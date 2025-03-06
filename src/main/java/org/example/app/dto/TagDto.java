package org.example.app.dto;

import lombok.Getter;
import org.example.app.entity.File;

import java.util.Set;

@Getter
public class TagDto {
    private Long id;
    private String name;
    private Set<File> files;

    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
