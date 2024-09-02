package com.dunice.GoncharovVVAdvancedServer.dto.response.Base;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BaseSuccessResponse {

    private Integer statusCode = 0;

    private final Boolean success = true;

    public BaseSuccessResponse(Integer code) {
        this.statusCode = code;
    }
}