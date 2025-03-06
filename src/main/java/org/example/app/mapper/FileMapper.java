package org.example.app.mapper;

import org.example.app.dto.FIleDto;
import org.example.app.entity.File;
import org.springframework.stereotype.Component;


@Component
public class FileMapper {
    public FIleDto toDto(File file) {
        Long id = file.getId();
        String name = file.getName();
        int capacity = file.getCapacity();

        return new FIleDto(id, name, capacity);
    }

    public File toFile(FIleDto fIleDto) {
        return new File(fIleDto.getName(), fIleDto.getCapacity());
    }
}



