package org.example.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;

import org.example.app.AppApplication;
import org.example.app.entity.File;
import org.example.app.exception.FileMemoryOverflowException;
import org.example.app.exception.FileNotFoundException;
import org.example.app.security.SecurityConfig;
import org.example.app.service.FilesService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FilesController.class)
@ContextConfiguration(classes = {AppApplication.class, SecurityConfig.class})
class FileControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FilesService fileService;

    private static final File MOCK_FILE = new File("Test file.txt", 1024);
    private static final String FILE_JSON = "{ \"fileName\": \"Test file.txt\",\"capacity\": 1024}";
    private static final String BIG_FILE_JSON = "{ \"fileName\": \"Test file.txt\",\"capacity\": 1_000_000_000}";


    @Test

    public void shouldSuccessfullyFindFile() throws Exception {
        when(fileService.getFile(any(String.class))).thenReturn(MOCK_FILE);
        mockMvc
                .perform(get("/second-memory/files/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    public void shouldFailToFindFile() throws Exception {
        when(fileService.getFile(any(String.class))).thenThrow(FileNotFoundException.class);
        mockMvc.perform(get("/second-memory/Test")).andExpect(status().isNotFound());
    }

    @Test
    public void shouldSuccessfullyReturnFiles() throws Exception {
        ArrayList<String> mockList = new ArrayList<>(Arrays.asList("0", "1"));
        when(fileService.getAllFiles()).thenReturn(mockList);
        mockMvc.perform(get("/second-memory/files")).andExpect(status().isOk());
    }

    @Test
    public void shouldSuccessfullyUploadFile() throws Exception {
        doNothing().when(fileService).uploadFile(new File(any(String.class), 1024));
        mockMvc
                .perform(post("/second-memory/files/upload").contentType("application/json").content(FILE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileName").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    public void shouldFailUploadFile() throws Exception {
        doThrow(FileMemoryOverflowException.class)
                .when(fileService)
                .uploadFile(new File(any(String.class), 1024));
        mockMvc
                .perform(post("/second-memory/files/upload").contentType("application/json").content(BIG_FILE_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSuccessfullyUpdateFile() throws Exception {
        doNothing().when(fileService).patchFile(any(String.class), any(File.class));
        mockMvc
                .perform(patch("/second-memory/files/patch/1").contentType("application/json").content(FILE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    public void shouldFailToUpdateFile() throws Exception {
        doThrow(FileNotFoundException.class).when(fileService).patchFile(any(String.class), any(File.class));
        mockMvc
                .perform(patch("/second-memory/files/patch/10").contentType("application/json").content(FILE_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSuccessfullyDeleteFile() throws Exception {
        when(fileService.deleteFile(any(String.class))).thenReturn(MOCK_FILE);
        mockMvc
                .perform(delete("/second-memory/files/delete/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("Test file.txt"))
                .andExpect(jsonPath("$.capacity").value(1024));
    }

    @Test
    public void shouldFailToDeleteFile() throws Exception {
        when(fileService.deleteFile(any(String.class))).thenThrow(FileNotFoundException.class);
        mockMvc.perform(delete("/second-memory/files/delete/12")).andExpect(status().isNotFound());
    }
}