package com.dunice.GoncharovVVAdvancedServer.exeception;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCodes errorCodes;
}