package com.pitercoding.backend.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.pitercoding.backend.domain.DespesaConsolidada;
import com.pitercoding.backend.domain.DespesaAgregada;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
public class CsvService {

    public List<String[]> readCsv(Path arquivo) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(arquivo.toFile()))) {
            return reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCsv(List<DespesaConsolidada> despesas, Path arquivo) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(arquivo.toFile()))) {
            writer.writeNext(new String[]{
                    "RegistroANS", "CNPJ", "RazaoSocial", "Ano", "Trimestre", "Valor", "UF", "NomeFantasia", "Modalidade"
            });

            for (DespesaConsolidada d : despesas) {
                writer.writeNext(new String[]{
                        d.getRegistroAns(),
                        d.getCnpj(),
                        d.getRazaoSocial(),
                        String.valueOf(d.getAno()),
                        String.valueOf(d.getTrimestre()),
                        d.getValor().toString(),
                        d.getUf(),
                        d.getNomeFantasia(),
                        d.getModalidade()
                });
            }
        }
    }

    public void writeAggregatedCsv(List<DespesaAgregada> agregadas, Path arquivo) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(arquivo.toFile()))) {
            writer.writeNext(new String[]{
                    "CNPJ", "RazaoSocial", "UF", "Total", "Media", "DesvioPadrao"
            });
            for (DespesaAgregada d : agregadas) {
                writer.writeNext(new String[]{
                        d.getCnpj(),
                        d.getRazaoSocial(),
                        d.getUf(),
                        d.getTotal().toString(),
                        d.getMedia().toString(),
                        d.getDesvioPadrao().toString()
                });
            }
        }
    }
}

