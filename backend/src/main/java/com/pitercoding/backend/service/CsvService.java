package com.pitercoding.backend.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.pitercoding.backend.domain.Despesa;
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

    public void writeCsv(List<Despesa> despesas, Path arquivo) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(arquivo.toFile()))) {
            writer.writeNext(new String[]{
                    "CNPJ",
                    "RazaoSocial",
                    "Ano",
                    "Trimestre",
                    "Valor",
                    "UF",
                    "NomeFantasia",
                    "Modalidade"
            });
            for (Despesa d : despesas) {
                writer.writeNext(d.toArray());
            }
        }
    }
}

