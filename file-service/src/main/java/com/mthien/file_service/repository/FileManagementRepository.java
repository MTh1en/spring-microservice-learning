package com.mthien.file_service.repository;

import com.mthien.file_service.entity.FileManagement;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagementRepository extends MongoRepository<FileManagement, String> {
    
}
