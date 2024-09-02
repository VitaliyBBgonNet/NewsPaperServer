package com.dunice.GoncharovVVAdvancedServer.dto.response.castom;

public class CreateNewsSuccessResponse {

    private Integer id;

    private Integer statusCode;

    private Boolean success;

    public CreateNewsSuccessResponse(Integer id) {
        this.id = id;
    }
}
