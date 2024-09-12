package com.dunice.GoncharovVVAdvancedServer.constants;

public interface StringConstants {
    String BEARER = "Bearer ";

    String AUTHORIZATION = "Authorization";

    String PATTERN_FORMAT_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    String WAY_FOR_UPLOAD_FILE = "uploadsFiles/";

    String FILE_UPLOAD_SUCCESSFUL = "File uploaded successfully: ";

    String FAILED_UPLOAD_FILE = "File upload failed: ";

    String HTTP_WAY_TO_FILES = "http://localhost:8080/v1/file/";
}