package com.dunice.GoncharovVVAdvancedServer.dto.response;

import lombok.Data;

@Data
public class PublicUserResponse {
    private String avatar;

    private String email;

    private String id;
    
    private String name;

    private String role;
}