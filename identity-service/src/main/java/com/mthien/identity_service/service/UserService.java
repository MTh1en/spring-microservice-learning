package com.mthien.identity_service.service;

import com.mthien.identity_service.constant.PredefinedRole;
import com.mthien.identity_service.entity.Role;
import com.mthien.identity_service.entity.Users;
import com.mthien.identity_service.mapper.UserMapper;
import com.mthien.identity_service.payload.user.CreateUserRequest;
import com.mthien.identity_service.payload.user.UserResponse;
import com.mthien.identity_service.repository.RoleRepository;
import com.mthien.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserResponse createUser(CreateUserRequest request) {
        Users newUser = userMapper.createUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        newUser.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    @PostAuthorize("returnObject.id == authentication.getName()")
    public UserResponse getUserById(String id) {
        log.info("authentication name: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return userMapper.toUserResponse(userRepository.findById(id).orElse(null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
}
