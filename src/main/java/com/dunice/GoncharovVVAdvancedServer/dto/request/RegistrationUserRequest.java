package com.dunice.GoncharovVVAdvancedServer.dto.request;

import com.dunice.GoncharovVVAdvancedServer.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserRequest {

    @NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;

    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @NotBlank(message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @Size(min = 3, max = 25, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    private String name;

    @Size(min = 3, max = 25, message = ValidationConstants.PASSWORD_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_PASSWORD_NULL)
    private String password;

    @NotBlank(message = ValidationConstants.USER_ROLE_NOT_NULL)
    @Size(min = 3, max = 100, message = ValidationConstants.ROLE_SIZE_NOT_VALID)
    private String role;
}