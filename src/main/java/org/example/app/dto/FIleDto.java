package org.example.app.dto;

import lombok.Getter;
import org.example.app.entity.Tag;

import java.util.HashSet;
import java.util.Set;

@Getter
public class FIleDto {
    private Long id;
    private String name;
    private int capacity;
    private final Set<Tag> tags = new HashSet<>();

    public FIleDto(Long id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
}
