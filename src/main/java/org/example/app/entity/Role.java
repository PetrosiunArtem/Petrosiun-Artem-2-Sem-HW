package org.example.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Role", description = "Сущность Роли")
public record Role(
        @Schema(description = "Название роли", example = "admin", type = "String") String roleName) {
}
