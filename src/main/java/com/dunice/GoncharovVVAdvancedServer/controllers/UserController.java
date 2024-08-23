package com.dunice.GoncharovVVAdvancedServer.controllers;

import com.dunice.GoncharovVVAdvancedServer.dto.request.RegistrationUserDtoRequest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.LoginUserDtoResponse;
import com.dunice.GoncharovVVAdvancedServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<LoginUserDtoResponse> registrationUser(
            @RequestBody
            RegistrationUserDtoRequest registration) {

        return null;
    }






}
