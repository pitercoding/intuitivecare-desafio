package com.pitercoding.backend.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ZipService {
    public List<Path> extractZip(Path zipFile) throws IOException {
        List<Path> arquivos = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path destino = Paths.get("tmp", entry.getName());
                Files.copy(zis, destino, StandardCopyOption.REPLACE_EXISTING);
                arquivos.add(destino);
            }
        }
        return arquivos;
    }
}
