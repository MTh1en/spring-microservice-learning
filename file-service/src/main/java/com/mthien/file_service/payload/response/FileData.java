package com.mthien.file_service.payload.response;

import org.springframework.core.io.Resource;

public record FileData(String contentType, Resource resource) {

}
