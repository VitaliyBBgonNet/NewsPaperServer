package com.dunice.GoncharovVVAdvancedServer.dto.response.castom;

import lombok.Getter;

@Getter
public class CreateNewsSuccessResponse {

    private Integer id;

    private Integer statusCode = 0;

    private Boolean success = true;

    public CreateNewsSuccessResponse(Integer id) {
        this.id = id;
    }
}
