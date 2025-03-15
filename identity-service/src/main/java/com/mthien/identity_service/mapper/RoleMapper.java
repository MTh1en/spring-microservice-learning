package com.mthien.identity_service.mapper;

import com.mthien.identity_service.entity.Role;
import com.mthien.identity_service.payload.role.RoleRequest;
import com.mthien.identity_service.payload.role.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
