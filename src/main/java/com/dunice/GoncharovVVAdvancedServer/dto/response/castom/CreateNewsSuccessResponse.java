package com.dunice.GoncharovVVAdvancedServer.dto.response.castom;

import lombok.Getter;

@Getter
public class CreateNewsSuccessResponse {

    private Long id;

    private Integer statusCode = 0;

    private Boolean success = true;

    public CreateNewsSuccessResponse(Long id) {
        this.id = id;
    }
}