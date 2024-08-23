package com.dunice.GoncharovVVAdvancedServer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDtoRequest {
    private String avatar;

    private String email;

    private String name;

    private String password;

    private String role;

}
