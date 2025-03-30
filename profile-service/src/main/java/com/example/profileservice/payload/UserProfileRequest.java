package com.example.profileservice.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {
    String userId;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    String address;
    LocalDate dob;
}
