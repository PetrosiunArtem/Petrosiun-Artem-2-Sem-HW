package org.example.app.mapper;

import org.example.app.dto.FileDto;
import org.example.app.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public FileDto toDto(File file) {
        Long id = file.getId();
        String name = file.getName();
        int capacity = file.getCapacity();
 
        return new FileDto(id, name, capacity);
    }

    public File toFile(FileDto fIleDto) {
        return new File(fIleDto.name(), fIleDto.capacity());
    }
}



