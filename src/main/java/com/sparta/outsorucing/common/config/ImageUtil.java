package com.sparta.outsorucing.common.config;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j(topic = "ImageUtil")
@Component
@RequiredArgsConstructor
public class ImageUtil {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucket;
    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";
    private final Storage storage;

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String uuid = UUID.randomUUID().toString();
        String fileType = file.getContentType();
        if (!(fileType.endsWith("jpg") || fileType.endsWith("png") || fileType.endsWith("jpeg") || fileType.startsWith("image"))) {
            throw new InvalidRequestException("허용하지 않는 파일 형식입니다.");
        }
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket,
                                                uuid)
            .setContentType(fileType)
            .build();
        try {
            storage.create(blobInfo, file.getBytes());
        } catch (IOException e) {
            throw new InvalidRequestException("ImageUtil storage.create 메서드 에러");
        }
        return IMAGE_URI_PREFIX + bucket + "/" + uuid;
    }

    public void deleteImage(String imageUri) {
        String uuid = UUID.randomUUID().toString();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket,uuid).build();
        storage.delete(bucket, uuid);
    }
}
