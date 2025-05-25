package org.example.filestorage.service;

import org.example.filestorage.entity.FileEntity;
import org.example.filestorage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileRepository repository;

    @Value("${storage.base-path}")
    private String basePath;

    public FileEntity store(MultipartFile file) throws IOException {
        FileEntity entity = new FileEntity();
        entity.setName(file.getOriginalFilename());
        repository.save(entity);

        String location = basePath + File.separator + entity.getId() + "_" + file.getOriginalFilename();
        Path path = Paths.get(location);

        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path);

        entity.setLocation(location);
        return repository.save(entity);
    }

    public File retrieve(Long id)  {
        FileEntity file = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
        return new File(file.getLocation());
    }
}
