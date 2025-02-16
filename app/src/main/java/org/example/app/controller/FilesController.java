package org.example.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;


@RequestMapping("/second-memory")
@Tag(name = "File API", description = "Управление пользователями")
public interface FilesController {

    @Operation(summary = "Скачать файл из репозитория")
    @ApiResponse(
            responseCode = "200",
            description = "Файл скачен",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content
    )
    public ResponseEntity<String> download(@PathVariable String fileId, @PathVariable String userId) throws MalformedURLException, FileNotFoundException;

    @Operation(summary = "Загрузить файл в репозиторий")
    @ApiResponse(
            responseCode = "200",
            description = "Файл загружен",
            content = @Content
    )
    @ApiResponse(
            responseCode = "400",
            description = "MEMORY_OVERFLOW | Файл превышает допустимый лимит по памяти",
            content = @Content
    )
    public ResponseEntity<String> postUploadPage(@PathVariable String bucketName, MultipartFile multipartFile) throws IOException, FileMemoryOverflowException;


    @Operation(summary = "Отобразить страницу по загрузке файла")
    @ApiResponse(
            responseCode = "200",
            description = "Страница отобразилась",
            content = @Content
    )
    public ResponseEntity<String> getUploadPage(@PathVariable String bucketName);
}
