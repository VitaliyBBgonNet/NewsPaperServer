package com.dunice.GoncharovVVAdvancedServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDtoResponse {
    String avatar;
    String email;
    String id;
    String name;
    String role;
//    String token;
}
