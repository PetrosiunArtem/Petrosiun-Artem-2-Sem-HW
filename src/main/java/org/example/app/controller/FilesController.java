package org.example.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.util.List;


@RequestMapping("/second-memory")
@Tag(name = "File API", description = "Управление пользователями")
public interface FilesController {

    @Operation(summary = "Скачать файл из репозитория")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл скачен",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND | Файл с такими данными не найден",
                    content = @Content
            )
    })
    ResponseEntity<String> downloadFile(@PathVariable String fileId, @PathVariable String userId) throws MalformedURLException;

    @Operation(summary = "Загрузить файл в репозиторий")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл загружен",
                    content = @Content
            ), @ApiResponse(
            responseCode = "400",
            description = "MEMORY_OVERFLOW | Файл превышает допустимый лимит по памяти",
            content = @Content
    )
    })
    ResponseEntity<File> postUploadPage(@RequestBody File file) throws FileMemoryOverflowException;

    @Operation(summary = "Взять файл по данному fileId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл найден",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND | Файл с такими данными не найден",
                    content = @Content
            )
    })
    ResponseEntity<File> getFile(@PathVariable String fileId) throws FileNotFoundException;

    @Operation(summary = "Отобразить страницу со всеми файлами")
    @ApiResponse(
            responseCode = "200",
            description = "Файлы отобразились"
    )
    ResponseEntity<List<String>> getAllFiles();

    @Operation(summary = "Удалить файл из репозитория")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл удален",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND | Файл с такими данными не найден",
                    content = @Content
            )
    })
    ResponseEntity<File> deleteFile(@PathVariable String fileId) throws FileNotFoundException;

    @Operation(summary = "Заменить файл из репозитория")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл заменен",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND | Файл с такими данными не найден",
                    content = @Content
            )
    })
    ResponseEntity<File> putFile(@PathVariable String fileId, @RequestBody File file) throws FileNotFoundException;

    @Operation(summary = "Изменить файл из репозитория")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл изменен",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND | Файл с такими данными не найден",
                    content = @Content
            )
    })
    ResponseEntity<File> patchFile(@PathVariable String fileId, @RequestBody File file) throws FileNotFoundException;
}
