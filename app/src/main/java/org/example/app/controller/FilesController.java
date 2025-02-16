package org.example.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    ResponseEntity<String> downloadFile(@PathVariable String fileId, @PathVariable String userId) throws MalformedURLException;

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
    ResponseEntity<String> postUploadPage(@PathVariable String bucketName, MultipartFile multipartFile) throws IOException, FileMemoryOverflowException;


    @Operation(summary = "Отобразить страницу по загрузке файла")
    @ApiResponse(
            responseCode = "200",
            description = "Страница отобразилась",
            content = @Content
    )
    ResponseEntity<String> getUploadPage(@PathVariable String bucketName);

    @Operation(summary = "Удалить файл из репозитория")
    @ApiResponse(
            responseCode = "200",
            description = "Файл удален",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content
    )
    ResponseEntity<File> deleteFile(@PathVariable String fileId) throws FileNotFoundException;

    @Operation(summary = "Заменить файл из репозитория")
    @ApiResponse(
            responseCode = "200",
            description = "Файл заменен",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content
    )
    ResponseEntity<File> putFile(@PathVariable String fileId, @RequestBody File file) throws FileNotFoundException;

    @Operation(summary = "Изменить файл из репозитория")
    @ApiResponse(
            responseCode = "200",
            description = "Файл изменен",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content
    )
    ResponseEntity<File> patchFile(@PathVariable String fileId, @RequestBody File file) throws FileNotFoundException;
}
