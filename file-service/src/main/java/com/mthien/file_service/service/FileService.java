package com.mthien.file_service.service;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mthien.file_service.exception.AppException;
import com.mthien.file_service.exception.ErrorCode;
import com.mthien.file_service.mapper.FileManagementMapper;
import com.mthien.file_service.payload.response.FileData;
import com.mthien.file_service.payload.response.FileResponse;
import com.mthien.file_service.repository.FileManagementRepository;
import com.mthien.file_service.repository.FileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    FileRepository fileRepository;
    FileManagementRepository fileManagementRepository;
    FileManagementMapper fileManagementMapper;

    public FileResponse uploadFile(MultipartFile file) throws IOException {
        var fileInfo = fileRepository.store(file);

        var fileManagement = fileManagementMapper.toFileManagement(fileInfo);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setOwnerId(userId);
        fileManagementRepository.save(fileManagement);

        return FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(fileInfo.getUrl())
                .build();
    }

    public FileData dowload(String fileName) throws IOException {
        var fileManagement = fileManagementRepository.findById(fileName).orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        var resource = fileRepository.read(fileManagement);

        return new FileData(fileManagement.getContentType(), resource);
    }
}
