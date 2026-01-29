package com.pitercoding.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

/**
 * Objetivo: Baixar cadastro de operadoras da ANS.
 */

@Service
public class OperadoraDownloadService {

    private final RestTemplate restTemplate;

    public OperadoraDownloadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Path downloadOperadoras() throws IOException {
        String url = "https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas.csv";
        Path target = Paths.get("tmp", "operadoras_ativas.csv");
        Files.createDirectories(target.getParent());

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return target;
    }
}
