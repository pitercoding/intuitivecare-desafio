package com.pitercoding.backend.service;

import com.opencsv.CSVReader;
import com.pitercoding.backend.domain.Operadora;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Objetivo: Carregar operadoras em memória (Map por CNPJ).
 * Transformar um arquivo de texto bruto (CSV baixado) em objetos Java que o sistema consegue entender e manipular.
 * Entrada: Um caminho de arquivo (Path).
 * Saída: Um mapa organizado com todas as operadoras na memória.
 */

@Service
public class OperadoraCsvService {

    private static final Logger log =
            LoggerFactory.getLogger(OperadoraCsvService.class);

    public Map<String, Operadora> loadOperadoras(Path arquivo) {
        Map<String, Operadora> mapa = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(arquivo.toFile()))) {
            String[] linha;
            reader.readNext(); // header

            while ((linha = reader.readNext()) != null) {

                if (linha.length < 5) {
                    log.warn("Linha inválida no CSV de operadoras");
                    continue;
                }

                String cnpj = linha[0].replaceAll("\\D", "");

                if (mapa.containsKey(cnpj)) continue;

                Operadora operadora = new Operadora();
                operadora.setCnpj(cnpj);
                operadora.setRazaoSocial(linha[1]);
                operadora.setNomeFantasia(linha[2]);
                operadora.setUf(linha[3]);
                operadora.setModalidade(linha[4]);

                mapa.put(cnpj, operadora);
            }
        } catch (Exception e) {
            log.error("Erro ao processar o arquivo CSV: {}", arquivo.getFileName(), e);
            throw new RuntimeException("Falha na importação dos dados das operadoras.");
        }

        return mapa;
    }
}
