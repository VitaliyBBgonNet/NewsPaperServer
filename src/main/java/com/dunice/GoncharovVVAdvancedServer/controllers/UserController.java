package com.dunice.GoncharovVVAdvancedServer.controllers;

import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserViewResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserViewResponse>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserViewResponse>> getUserById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserViewResponse>> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }
}
