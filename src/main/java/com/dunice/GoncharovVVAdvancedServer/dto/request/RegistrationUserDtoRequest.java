package com.dunice.GoncharovVVAdvancedServer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDtoRequest {
    String avatar;
    String email;
    String name;
    String password;
    String role;

}
