package com.dunice.GoncharovVVAdvancedServer.exeception;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

@ControllerAdvice
public class CustomErrorAdvice {

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handle(CustomException e) {
        Integer codes = e.getErrorCodes().getCode();
        return new ResponseEntity<>(new CustomSuccessResponse(codes), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();

        List<Integer> codes = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .map(ErrorCodes::getCodeByMessage)
                .toList();

        return new ResponseEntity<>(new CustomSuccessResponse(codes.get(0)), HttpStatus.BAD_REQUEST);
    }
}
