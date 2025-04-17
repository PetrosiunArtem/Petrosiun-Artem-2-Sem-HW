package org.example.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "file")
@Schema(name = "File", description = "Сущность Файла")
public class File {

    @Column(name = "name", nullable = false, length = 50)
    @NotNull(message = "File name have to be filled")
    @Setter
    @Schema(description = "Имя файла", example = "Red Hat.png", type = "String")
    private String name;

    @Column(name = "capacity", nullable = false)
    @NotNull(message = "File capacity have to be filled")
    @Schema(description = "Размер файла в байтах", example = "1024", type = "int")
    private int capacity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    protected File() {
    }

    public File(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
