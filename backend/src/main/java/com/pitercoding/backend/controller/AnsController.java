package com.pitercoding.backend.controller;

import com.pitercoding.backend.dto.AnsDownloadDto;
import com.pitercoding.backend.service.AnsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ans")
public class AnsController {

    private final AnsService ansService;

    public AnsController(AnsService ansService) {
        this.ansService = ansService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processar() throws IOException {
        ansService.processarTrimestres(getUltimos3Trimestres());
        return ResponseEntity.ok("CSV gerado com sucesso!");
    }

    private List<AnsDownloadDto> getUltimos3Trimestres() {
        // lógica para gerar DTOs
        return List.of();
    }
}
