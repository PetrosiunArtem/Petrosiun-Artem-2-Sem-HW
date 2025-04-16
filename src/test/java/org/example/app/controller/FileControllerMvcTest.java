package org.example.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;

import org.example.app.AppApplication;
import org.example.app.config.DatabaseConfig;
import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.mapper.FileMapper;
import org.example.app.security.SecurityConfig;
import org.example.app.service.FilesServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FilesController.class)
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppApplication.class, SecurityConfig.class})
class FileControllerMvcTest extends DatabaseConfig {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FilesServiceImpl fileService;

    @MockitoBean
    private FileMapper fileMapper;

    private static final String FILE_JSON = "{ \"id\":1,\"name\": \"Test file.txt\",\"capacity\": 1024}";
    private static final String BIG_FILE_JSON = "{ \"name\": \"Test file.txt\",\"capacity\": 1_000_000_000}";
    private static final FileDto MOCK_FILE_DTO = new FileDto(1L, "Test file.txt", 1024);

    @Test
    void shouldSuccessfullyFindFile() throws Exception {
        when(fileService.getFile(any(Long.class))).thenReturn(MOCK_FILE_DTO);
        mockMvc
                .perform(get("/second-memory/files/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    void shouldFailToFindFile() throws Exception {
        when(fileService.getFile(any(Long.class))).thenThrow(FileNotFoundException.class);
        mockMvc.perform(get("/second-memory/Test")).andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessfullyReturnFiles() throws Exception {
        ArrayList<Long> mockList = new ArrayList<>(Arrays.asList(0L, 1L));
        when(fileService.getAllFiles()).thenReturn(mockList);
        mockMvc.perform(get("/second-memory/files")).andExpect(status().isOk());
    }

    @Test
    void shouldSuccessfullyUploadFile() throws Exception {
        when(fileService.uploadFile(new File(any(String.class), 1024))).thenReturn(MOCK_FILE_DTO);
        mockMvc
                .perform(post("/second-memory/files/upload").contentType("application/json").content(FILE_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailUploadFile() throws Exception {
        doThrow(FileMemoryOverflowException.class)
                .when(fileService)
                .uploadFile(new File(any(String.class), 1024));
        mockMvc
                .perform(post("/second-memory/files/upload").contentType("application/json").content(BIG_FILE_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSuccessfullyUpdateFile() throws Exception {
        when(fileService.patchFile(any(Long.class), any(File.class))).thenReturn(MOCK_FILE_DTO);
        mockMvc
                .perform(patch("/second-memory/files/patch/1").contentType("application/json").content(FILE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    void shouldFailToUpdateFile() throws Exception {
        doThrow(FileNotFoundException.class).when(fileService).patchFile(any(Long.class), any(File.class));
        mockMvc
                .perform(patch("/second-memory/files/patch/10").contentType("application/json").content(FILE_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessfullyDeleteFile() throws Exception {
        when(fileService.deleteFile(any(Long.class))).thenReturn(MOCK_FILE_DTO);
        mockMvc
                .perform(delete("/second-memory/files/delete/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    void shouldFailToDeleteFile() throws Exception {
        when(fileService.deleteFile(any(Long.class))).thenThrow(FileNotFoundException.class);
        mockMvc.perform(delete("/second-memory/files/delete/12")).andExpect(status().isNotFound());
    }
}