package com.dunice.GoncharovVVAdvancedServer.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CustomSuccessResponse<T> {
    private T data;

    private Integer statusCode = 0;

    private Boolean success = true;

    private List<Integer> codes;

    public CustomSuccessResponse(T dataResponse) {
        this.data = (T) dataResponse;
    }

    public CustomSuccessResponse(Integer codes) {
        this.statusCode = codes;
    }

    public CustomSuccessResponse(Integer codes, List<Integer> listCodes) {
        this.statusCode = codes;
        this.codes = listCodes;
    }
}