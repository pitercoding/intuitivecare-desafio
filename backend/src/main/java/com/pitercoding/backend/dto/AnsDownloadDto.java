package com.pitercoding.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnsDownloadDto {
    private String url;
    private int ano;
    private int trimestre;
    private Path localFile; // caminho do ZIP baixado
}
