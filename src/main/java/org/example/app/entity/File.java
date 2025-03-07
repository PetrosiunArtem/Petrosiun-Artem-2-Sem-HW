package org.example.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "File", description = "Сущность Файла")
public record File(
        @Schema(description = "Имя файла", example = "Red Hat.png", type = "String") String fileName,
        @Schema(description = "Размер файла в байтах", example = "1024", type = "int") int capacity) {
}
