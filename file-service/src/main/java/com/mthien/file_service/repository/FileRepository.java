package com.mthien.file_service.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mthien.file_service.entity.FileManagement;
import com.mthien.file_service.payload.FileInfo;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FileRepository {
    @Value("${app.file.storage-dir}")
    String storageDir;

    @Value("${app.file.dowload-prefix}")
    String urlPrefix;

    public FileInfo store(MultipartFile file) throws IOException {
        Path folder = Paths.get(storageDir);

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = Objects.isNull(fileExtension)
                ? UUID.randomUUID().toString()
                : UUID.randomUUID().toString() + "." + fileExtension;
        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return FileInfo.builder()
                .name(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .md5CheckSum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(urlPrefix + fileName)
                .build();
    }

    public Resource read(FileManagement fileManagement) throws IOException {
        var data = Files.readAllBytes(Path.of(fileManagement.getPath()));
        return new ByteArrayResource(data);
    }
}
