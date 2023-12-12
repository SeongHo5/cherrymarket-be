package com.cherrydev.cherrymarketbe.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.cherrydev.cherrymarketbe.common.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static com.cherrydev.cherrymarketbe.common.service.FileService.BUCKET_NAME;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private AmazonS3Client objectStorageClient;

    @InjectMocks
    private FileService fileService;

    @Test
    void 파일_업로드_성공() throws IOException {
        // Given
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        String dirName = "testDir";

        // When
        when(objectStorageClient.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());
        when(objectStorageClient.getUrl(eq(BUCKET_NAME), anyString())).thenReturn(new URL("http://example.com/test.jpg")); // 예제 URL

        // When
        String result = fileService.uploadSingleFile(multipartFile, dirName);

        // Then
        assertThat(result).isNotNull();
    }

}
