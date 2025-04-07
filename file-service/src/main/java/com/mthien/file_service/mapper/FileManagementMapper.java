package com.mthien.file_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mthien.file_service.entity.FileManagement;
import com.mthien.file_service.payload.FileInfo;

@Mapper(componentModel = "spring")
public interface FileManagementMapper {
    @Mapping(target = "id", source = "name")
    @Mapping(target = "ownerId", ignore = true)
    FileManagement toFileManagement(FileInfo fileInfo);
}
