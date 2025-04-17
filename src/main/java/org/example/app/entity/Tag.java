package org.example.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
@Getter
@Schema(name = "Tag", description = "Сущность Тега")
public class Tag {
    @Schema(description = "Название тега", example = "secret", type = "String")
    @Column(name = "name", nullable = false, length = 50)
    @Setter
    @NotNull(message = "Tag name have to be filled")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    protected Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }
}
