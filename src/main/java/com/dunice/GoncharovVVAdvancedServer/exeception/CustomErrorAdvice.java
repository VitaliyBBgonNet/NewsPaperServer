package com.dunice.GoncharovVVAdvancedServer.exeception;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class CustomErrorAdvice {

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handle(CustomException customException) {
        Integer codes = customException.getErrorCodes().getCode();
        HttpHeaders headers = new HttpHeaders();
        headers.add("errorHeader", customException.getErrorCodes().getMessage());
        return new ResponseEntity<>(new CustomSuccessResponse(codes), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();

        List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        List<Integer> codes = errorMessages
                .stream()
                .map(ErrorCodes::getCodeByMessage)
                .toList();

        HttpHeaders headers = new HttpHeaders();
        headers.add("errorHeader", errorMessages.get(0));

        return new ResponseEntity<>(new CustomSuccessResponse(codes.get(0), codes), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> messageException = exception.getConstraintViolations();

        List<String> errorMessages = messageException.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .toList();

        List<Integer> codes = errorMessages.stream()
                .map(ErrorCodes::getCodeByMessage)
                .toList();

        HttpHeaders headers = new HttpHeaders();
        headers.add("errorHeader", errorMessages.get(0));

        return new ResponseEntity<>(new CustomSuccessResponse(codes.get(0), codes), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CustomSuccessResponse> handleMultipartException(MultipartException exception) {
        Integer code = ErrorCodes.getCodeByMessage(exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("errorHeader", exception.getMessage());
        return new ResponseEntity<>(new CustomSuccessResponse(code), HttpStatus.BAD_REQUEST);
    }
}