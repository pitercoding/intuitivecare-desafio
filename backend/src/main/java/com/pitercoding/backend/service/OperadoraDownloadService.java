package com.pitercoding.backend.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

/**
 * Objetivo: Baixar cadastro de operadoras da ANS.
 */

@Service
public class OperadoraDownloadService {

    private static final String URL_ANS = "https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas.csv";

    public Path downloadCadastro() throws IOException {
        Path destino = Paths.get("tmp", "operadoras.csv");
        Files.createDirectories(destino.getParent()); // se tmp não existir, será criada antes de iniciar o download

        try (InputStream in = new URL(URL_ANS).openStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        }
        return destino;
    }
}
