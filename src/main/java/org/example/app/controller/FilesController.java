package org.example.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.Part;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Tag(name = "File API", description = "Управление пользователями")
public interface FilesController {

  @Operation(summary = "Скачать файл из репозитория")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Файл скачен", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content)
      })
  @GetMapping("/files/info/download/")
  ResponseEntity<FileDto> downloadFile(@RequestBody Part file);

  @Operation(summary = "Загрузить файл в репозиторий")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Файл загружен", content = @Content),
        @ApiResponse(
            responseCode = "400",
            description = "MEMORY_OVERFLOW | Файл превышает допустимый лимит по памяти",
            content = @Content)
      })
  @PostMapping("/files/upload")
  ResponseEntity<FileDto> postUploadPage(@RequestBody File file) throws FileMemoryOverflowException;

  @Operation(summary = "Взять файл по данному fileId")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Файл найден", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content)
      })
  @GetMapping("/files/get/{fileId}")
  ResponseEntity<FileDto> getFile(@PathVariable(name = "fileId") Long fileId)
      throws FileNotFoundException;

  @Operation(summary = "Отобразить страницу со всеми файлами")
  @ApiResponse(responseCode = "200", description = "Файлы отобразились")
  @GetMapping("/files")
  ResponseEntity<List<Long>> getAllFiles();

  @Operation(summary = "Удалить файл из репозитория")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Файл удален", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content)
      })
  @DeleteMapping("/files/delete/{fileId}")
  ResponseEntity<FileDto> deleteFile(@PathVariable(name = "fileId") Long fileId)
      throws FileNotFoundException;

  @Operation(summary = "Заменить файл из репозитория")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Файл заменен", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content)
      })
  @PutMapping("/files/put/{fileId}")
  ResponseEntity<FileDto> putFile(
      @PathVariable(name = "fileId") Long fileId, @RequestBody File file)
      throws FileNotFoundException;

  @Operation(summary = "Изменить файл из репозитория")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Файл изменен", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND | Файл с такими данными не найден",
            content = @Content)
      })
  @PatchMapping("/files/patch/{fileId}")
  ResponseEntity<FileDto> patchFile(
      @PathVariable(name = "fileId") Long fileId, @RequestBody File file)
      throws FileNotFoundException;
}
