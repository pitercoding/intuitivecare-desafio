package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.Despesa;
import com.pitercoding.backend.dto.AnsDownloadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.YearMonth;

/**
 * AnsService orquestra todo o processo:
 * Identifica últimos 3 trimestres
 * Faz download dos ZIPs
 * Extrai os arquivos
 * Lê CSV/XLSX
 * Normaliza os dados
 * Gera consolidado_despesas.csv
 *
 * Resumo do fluxo:
 * [AnsController] --> [AnsService] --> [FileDownloadService] --> [ZipService] --> [CsvService/XlsxService] --> [Despesa]
 *                                                         \
 *                                                          --> [consolidado_despesas.csv]
 */

@Service
public class AnsService {

    private static final Logger log = LoggerFactory.getLogger(AnsService.class);

    private final FileDownloadService fileDownloadService;
    private final ZipService zipService;
    private final CsvService csvService;
    private final XlsxService xlsxService;

    public AnsService(FileDownloadService f, ZipService z, CsvService c, XlsxService x) {
        this.fileDownloadService = f;
        this.zipService = z;
        this.csvService = c;
        this.xlsxService = x;
    }

    public void processarTrimestres(List<AnsDownloadDto> dtos) throws IOException {
        // garante que a pasta tmp existe
        Files.createDirectories(Paths.get("tmp"));

        List<Despesa> consolidado = new ArrayList<>();

        for (AnsDownloadDto dto : dtos) {
            log.info("Processando {} Q{}", dto.getAno(), dto.getTrimestre());

            // Download ZIP
            Path zip = fileDownloadService.downloadZip(dto.getUrl(),
                    dto.getAno() + "_Q" + dto.getTrimestre() + ".zip");

            // Extração ZIP
            List<Path> arquivos = zipService.extractZip(zip);

            for (Path arquivo : arquivos) {
                try {
                    if (arquivo.toString().endsWith(".csv")) {
                        csvService.readCsv(arquivo).forEach(linha -> {
                            try {
                                // Normalização
                                String cnpj = linha[0].replaceAll("\\D", "");
                                String razao = linha[1].trim();
                                int ano = Integer.parseInt(linha[2]);
                                int trimestre = Integer.parseInt(linha[3]);
                                BigDecimal valor = new BigDecimal(linha[4]);

                                consolidado.add(new Despesa(cnpj, razao, ano, trimestre, valor));
                            } catch (Exception e) {
                                log.warn("Linha inválida no CSV {}: {}", arquivo.getFileName(), Arrays.toString(linha));
                            }
                        });
                    } else if (arquivo.toString().endsWith(".xlsx")) {
                        xlsxService.readXlsx(arquivo).forEach(despesa -> {
                            // Normalização de XLSX (mesmo que CSV)
                            String cnpj = despesa.getCnpj().replaceAll("\\D", "");
                            String razao = despesa.getRazaoSocial().trim();
                            consolidado.add(new Despesa(cnpj, razao, despesa.getAno(),
                                    despesa.getTrimestre(), despesa.getValor()));
                        });
                    }
                } catch (Exception e) {
                    log.error("Erro ao processar arquivo {}: {}", arquivo.getFileName(), e.getMessage());
                }
            }
        }

        // Grava CSV consolidado
        Path consolidadoCsv = Paths.get("consolidado_despesas.csv");
        csvService.writeCsv(consolidado, consolidadoCsv);
        log.info("CSV consolidado gerado em {}", consolidadoCsv.toAbsolutePath());
    }

    // Gera os últimos 3 trimestres (stub para URLs, você deve ajustar conforme ANS)
    public List<AnsDownloadDto> getUltimos3Trimestres() {
        YearMonth agora = YearMonth.now();
        List<AnsDownloadDto> dtos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            YearMonth ym = agora.minusMonths(i * 3L);
            int ano = ym.getYear();
            int trimestre = (ym.getMonthValue() - 1) / 3 + 1;
            String url = construirUrlDoZip(ano, trimestre);
            dtos.add(new AnsDownloadDto(url, ano, trimestre, null));
        }
        return dtos;
    }

    // Stub para URL da ANS
    private String construirUrlDoZip(int ano, int trimestre) {
        // Ajuste conforme o padrão do site da ANS
        return "https://www.ans.gov.br/dados/zip_" + ano + "_Q" + trimestre + ".zip";
    }
}
