package org.example.app.dto;

import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Getter;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;


//@Getter

public record TagDto(Long id, String name) {
//    private Long id;
//    private String name;
//
//    public TagDto(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }
}
