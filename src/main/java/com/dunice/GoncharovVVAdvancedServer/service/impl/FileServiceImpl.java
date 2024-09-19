package com.dunice.GoncharovVVAdvancedServer.service.impl;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.dto.response.common.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.service.FileService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@Service
public class FileServiceImpl implements FileService {

    @Value("${http.way}")
    private String httpWay;

    @Value("${way.upload}")
    private String wayUpload;

    public CustomSuccessResponse<String> uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
        }

        try {
            File directoryForUploadFile = new File(wayUpload);

            if (!directoryForUploadFile.exists()) {
                directoryForUploadFile.mkdir();
            }

            Path path = Paths.get(wayUpload + file.getOriginalFilename());
            file.transferTo(path);
            return new CustomSuccessResponse<>(httpWay + file.getOriginalFilename());
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.FAILED_UPLOAD_FILE);
        }
    }

    @Override
    public UrlResource giveFile(String fileName) {
        try {
            Path filePath = Paths.get(wayUpload).resolve(fileName).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
            }
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCodes.FAILED_UPLOAD_FILE);
        }
    }
}