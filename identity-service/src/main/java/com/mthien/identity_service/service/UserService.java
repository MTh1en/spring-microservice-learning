package com.mthien.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mthien.event.dto.NotificationEvent;
import com.mthien.identity_service.constant.PredefinedRole;
import com.mthien.identity_service.entity.Role;
import com.mthien.identity_service.entity.Users;
import com.mthien.identity_service.mapper.ProfileMapper;
import com.mthien.identity_service.mapper.UserMapper;
import com.mthien.identity_service.payload.user.CreateUserRequest;
import com.mthien.identity_service.payload.user.UserResponse;
import com.mthien.identity_service.repository.RoleRepository;
import com.mthien.identity_service.repository.UserRepository;
import com.mthien.identity_service.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    ProfileClient profileClient;
    ProfileMapper profileMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public UserResponse createUser(CreateUserRequest request) {
        Users newUser = userMapper.createUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        newUser.setRoles(roles);

        newUser = userRepository.save(newUser);
        var profileRequest = profileMapper.toProfileRequest(request);
        profileRequest.setUserId(newUser.getId());
        profileClient.createProfile(profileRequest);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recepient(request.getEmail())
                .teemplateCode("welcome")
                .param(null)
                .subject("Welcome to our service")
                .body("Welcome to our service, " + newUser.getUsername())
                .build();
                
        //Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);
        
        return userMapper.toUserResponse(newUser);
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
