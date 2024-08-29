package com.dunice.GoncharovVVAdvancedServer.exeception;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;

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

        return new ResponseEntity<>(new CustomSuccessResponse(codes.get(0), codes), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> messageException = exception.getConstraintViolations();
        List<Integer> codes = messageException.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .map(ErrorCodes::getCodeByMessage).toList();

        return new ResponseEntity<>(new CustomSuccessResponse(codes.get(0), codes), HttpStatus.BAD_REQUEST);
    }
}