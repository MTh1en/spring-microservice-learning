package com.mthien.identity_service.repository;

import com.mthien.identity_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

}
