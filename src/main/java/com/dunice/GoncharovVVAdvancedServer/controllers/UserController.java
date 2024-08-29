package com.dunice.GoncharovVVAdvancedServer.controllers;

import com.dunice.GoncharovVVAdvancedServer.constants.ValidationConstants;
import com.dunice.GoncharovVVAdvancedServer.dto.response.PublicUserResponse;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.service.UserService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserById(
            @PathVariable
            @Size(min = 36, max = 36, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED)
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                    message = ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION) String id) {
        return ResponseEntity.ok(userService.getUserById(UUID.fromString(id)));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }
}