package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.DespesaConsolidada;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class XlsxService {
    public List<DespesaConsolidada> readXlsx(Path arquivo) throws IOException {
        List<DespesaConsolidada> despesas = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(arquivo.toFile());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            String cnpj = row.getCell(0).getStringCellValue();
            String razao = row.getCell(1).getStringCellValue();
            int ano = (int) row.getCell(2).getNumericCellValue();
            int trimestre = (int) row.getCell(3).getNumericCellValue();
            BigDecimal valor = BigDecimal.valueOf(row.getCell(4).getNumericCellValue());
            despesas.add(new DespesaConsolidada(cnpj, razao, ano, trimestre, valor, null));
        }
        return despesas;
    }
}
