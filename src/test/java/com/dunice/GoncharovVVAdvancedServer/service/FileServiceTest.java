package com.dunice.GoncharovVVAdvancedServer.service;

import com.dunice.GoncharovVVAdvancedServer.constants.ErrorCodes;
import com.dunice.GoncharovVVAdvancedServer.constantsTest.ConstantsTest;
import com.dunice.GoncharovVVAdvancedServer.dto.response.castom.CustomSuccessResponse;
import com.dunice.GoncharovVVAdvancedServer.exeception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import com.dunice.GoncharovVVAdvancedServer.service.impl.FileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileServiceTest {

    @Mock
    private MultipartFile file;

    @InjectMocks
    private FileServiceImpl fileService;

    private String fileName;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        setupTestDirectory();
        initializeFileServiceFields();
        fileName = ConstantsTest.NAME_TEST_FILE;
    }

    private void setupTestDirectory() {
        File testDirectory = new File(ConstantsTest.UPLOAD);
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
    }

    private void initializeFileServiceFields() throws NoSuchFieldException, IllegalAccessException {
        Field httpWayField = FileServiceImpl.class.getDeclaredField(ConstantsTest.HTTP_WAY);
        httpWayField.setAccessible(true);
        httpWayField.set(fileService, ConstantsTest.LOCAL_HOST);

        Field wayUploadField = FileServiceImpl.class.getDeclaredField(ConstantsTest.UPLOAD_WAY);
        wayUploadField.setAccessible(true);
        wayUploadField.set(fileService, ConstantsTest.UPLOAD);
    }

    @Test
    public void testUploadFileSuccess() throws IOException {
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.isEmpty()).thenReturn(false);

        File testFile = new File(fileService.getWayUpload(), fileName);
        testFile.createNewFile();
        testFile.deleteOnExit();

        CustomSuccessResponse<String> response = fileService.uploadFile(file);

        assertNotNull(response);
        assertEquals(fileService.getHttpWay() + fileName, response.getData());

        verify(file).transferTo(any(Path.class));
    }

    @Test
    public void testUploadFileThrowsExceptionWhenFileIsEmpty() {
        when(file.isEmpty()).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            fileService.uploadFile(file);
        });

        assertEquals(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, exception.getErrorCodes());
    }

    @Test
    public void testUploadFileThrowsExceptionOnIOException() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn(fileName);
        doThrow(new IOException()).when(file).transferTo(any(Path.class));

        CustomException exception = assertThrows(CustomException.class, () -> {
            fileService.uploadFile(file);
        });

        assertEquals(ErrorCodes.FAILED_UPLOAD_FILE, exception.getErrorCodes());
    }
}
