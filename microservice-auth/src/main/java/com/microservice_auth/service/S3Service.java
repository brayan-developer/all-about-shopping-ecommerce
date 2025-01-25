package com.microservice_auth.service;

import com.microservice_auth.exceptions.FileUploadException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.bucket}") String bucketName
    ) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build();
        this.bucketName = bucketName;
    }

    private boolean isImage(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/webp") ||
                contentType.equals("image/jpg");
    }

    public String uploadFile(MultipartFile file)  {
        try {
            validateFile(file);
            String fileName = generateFileName(file);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        }catch (FileUploadException e){
            throw new FileUploadException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void deletePreviousImageFromS3(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains(bucketName)) {
            return ;
        }
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete S3 object: " + e.getMessage());
        }
    }


    private void validateFile(MultipartFile file){
        String contentType = file.getContentType();
        if (contentType == null || !isImage(contentType) || !isValidImage(file)) {
            throw new FileUploadException("Only image files are allowed");
        }

        long maxFileSize = 2 * 1024 * 1024; // 2 MB
        if (file.getSize() > maxFileSize) {
            throw new FileUploadException("File size exceeds the 2MB limit");
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll(" ", "_");
    }

    private boolean isValidImage(MultipartFile file) {
        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getInputStream());

            return detectedType.startsWith("image/");
        } catch (IOException e) {
            throw new FileUploadException("Invalid image file content");
        }
    }
}