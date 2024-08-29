package com.dunice.GoncharovVVAdvancedServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserViewResponse {
    private String avatar;

    private String email;

    private String id;
    
    private String name;

    private String role;
}
