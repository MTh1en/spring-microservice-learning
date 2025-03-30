package com.mthien.identity_service.payload.profile;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String id;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    String address;
    LocalDate dob;
}
