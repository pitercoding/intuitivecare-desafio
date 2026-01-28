package com.pitercoding.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileDownloadService {
    private final RestTemplate restTemplate;

    public FileDownloadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Path downloadZip(String url, String nomeArquivo) throws IOException {
        Path target = Paths.get("tmp", nomeArquivo);
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return target;
    }
}
