package com.sparta.outsorucing.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class StorageConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String location;

    @Bean
    public Storage storage() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(location).openStream();
        return StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(keyFile))
            .build()
            .getService();
    }
}
