package com.mthien.identity_service.payload.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    String email;
    String username;
    String password;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    String address;
    LocalDate dob;
    boolean isActive;
    boolean isGoogle;
    boolean isFacebook;
    boolean isVerified;
}
