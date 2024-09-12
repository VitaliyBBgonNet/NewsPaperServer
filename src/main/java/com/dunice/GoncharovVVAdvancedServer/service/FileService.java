package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    CustomSuccessResponse<String> uploadFile(MultipartFile file);

    UrlResource giveFile(String fileName);
}
