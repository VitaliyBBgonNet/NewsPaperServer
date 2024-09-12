package com.dunice.GoncharovVVAdvancedServer.service.impl;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.constants.StringConstants;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import com.dunice.GoncharovVVAdvancedServer.service.FileService;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    public CustomSuccessResponse<String> uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
        }

        try {
            File directoryForUploadFile = new File(StringConstants.WAY_FOR_UPLOAD_FILE);

            if (!directoryForUploadFile.exists()) {
                directoryForUploadFile.mkdir();
            }

            Path path = Paths.get(StringConstants.WAY_FOR_UPLOAD_FILE + file.getOriginalFilename());
            file.transferTo(path);
            return new CustomSuccessResponse<>(StringConstants.HTTP_WAY_TO_FILES + file.getOriginalFilename());
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.FAILED_UPLOAD_FILE);
        }
    }

    @Override
    public UrlResource giveFile(String fileName) {
        try {
            Path filePath = Paths.get(StringConstants.WAY_FOR_UPLOAD_FILE).resolve(fileName).normalize();
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
