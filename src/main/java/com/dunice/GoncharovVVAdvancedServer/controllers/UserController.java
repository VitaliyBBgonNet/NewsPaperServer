package com.dunice.GoncharovVVAdvancedServer.controllers;

import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserView;
import com.dunice.GoncharovVVAdvancedServer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity getUsersList(){
        return ResponseEntity.ok("HelloWorld");
    }




}
