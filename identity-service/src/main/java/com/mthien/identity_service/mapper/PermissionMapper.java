package com.mthien.identity_service.mapper;

import com.mthien.identity_service.entity.Permission;
import com.mthien.identity_service.payload.permission.PermissionRequest;
import com.mthien.identity_service.payload.permission.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
