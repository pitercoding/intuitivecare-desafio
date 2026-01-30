package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.DespesaConsolidada;
import com.pitercoding.backend.domain.DespesaAgregada;
import com.pitercoding.backend.domain.Operadora;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * AnsService orquestra todo o fluxo de processamento de despesas da ANS:
 * - Identifica os últimos 3 trimestres
 * - Baixa e extrai os arquivos ZIP de despesas
 * - Lê dados de CSV e XLSX
 * - Valida e enriquece despesas com informações de operadoras
 * - Gera CSV consolidado e CSV de registros sem match
 * - Calcula agregações financeiras (total, média, desvio padrão)
 * - Compacta o CSV final em ZIP
 *
 * Fluxo resumido:
 * [AnsController] -> [AnsService] -> [FileDownloadService, ZipService, CsvService/XlsxService, DespesaEnrichmentService] -> [consolidado_despesas.csv, despesas_agregadas.zip]
 */

@Service
public class AnsService {

    private static final Logger log = LoggerFactory.getLogger(AnsService.class);

    private final FileDownloadService fileDownloadService;
    private final ZipService zipService;
    private final CsvService csvService;
    private final XlsxService xlsxService;
    private final ValidationService validationService;
    private final OperadoraDownloadService operadoraDownloadService;
    private final OperadoraCsvService operadoraCsvService;
    private final AggregationService aggregationService;
    private final DespesaEnrichmentService despesaEnrichmentService;

    public AnsService(FileDownloadService fds,
                      ZipService zs,
                      CsvService cs,
                      XlsxService xs,
                      ValidationService vs,
                      OperadoraDownloadService ods,
                      OperadoraCsvService ocs,
                      AggregationService ags,
                      DespesaEnrichmentService desEnrich) {
        this.fileDownloadService = fds;
        this.zipService = zs;
        this.csvService = cs;
        this.xlsxService = xs;
        this.validationService = vs;
        this.operadoraDownloadService = ods;
        this.operadoraCsvService = ocs;
        this.aggregationService = ags;
        this.despesaEnrichmentService = desEnrich;
    }

    public void processarTrimestres(List<AnsDownloadDto> dtos) throws IOException {
        Files.createDirectories(Paths.get("tmp"));
        List<DespesaConsolidada> consolidado = new ArrayList<>();

        // ------------------
        // 1. Download + extração + leitura
        for (AnsDownloadDto dto : dtos) {
            log.info("Processando {} Q{}", dto.getAno(), dto.getTrimestre());
            Path zip = fileDownloadService.downloadZip(dto.getUrl(),
                    dto.getAno() + "_Q" + dto.getTrimestre() + ".zip");
            List<Path> arquivos = zipService.extractZip(zip);

            for (Path arquivo : arquivos) {
                try {
                    if (arquivo.toString().endsWith(".csv")) {
                        csvService.readCsv(arquivo).forEach(linha -> {
                            try {
                                String cnpj = linha[0].replaceAll("\\D", "");
                                String razao = linha[1].trim();
                                int ano = Integer.parseInt(linha[2]);
                                int trimestre = Integer.parseInt(linha[3]);
                                BigDecimal valor = new BigDecimal(linha[4]);
                                consolidado.add(new DespesaConsolidada(cnpj, razao, ano, trimestre, valor));
                            } catch (Exception e) {
                                log.warn("Linha inválida CSV {}: {}", arquivo.getFileName(), Arrays.toString(linha));
                            }
                        });
                    } else if (arquivo.toString().endsWith(".xlsx")) {
                        xlsxService.readXlsx(arquivo).forEach(despesa -> {
                            String cnpj = despesa.getCnpj().replaceAll("\\D", "");
                            String razao = despesa.getRazaoSocial().trim();
                            consolidado.add(new DespesaConsolidada(cnpj, razao, despesa.getAno(),
                                    despesa.getTrimestre(), despesa.getValor()));
                        });
                    }
                } catch (Exception e) {
                    log.error("Erro ao processar arquivo {}: {}", arquivo.getFileName(), e.getMessage());
                }
            }
        }

        // ------------------
        // Grava CSV consolidado
        Path consolidadoCsv = Paths.get("consolidado_despesas.csv");
        csvService.writeCsv(consolidado, consolidadoCsv);
        log.info("CSV consolidado gerado em {}", consolidadoCsv.toAbsolutePath());

        // ------------------
        // 2. Validação + join com operadoras
        Path operadorasCsv = operadoraDownloadService.downloadOperadoras();
        Map<String, Operadora> mapaOperadoras = operadoraCsvService.loadOperadoras(operadorasCsv);

        // Normalizar CNPJs do mapa e reconstruir o mapa para garantir match correto
        mapaOperadoras = mapaOperadoras.values().stream()
                .peek(op -> op.setCnpj(op.getCnpj().replaceAll("\\D", "")))
                .collect(Collectors.toMap(Operadora::getCnpj, op -> op));

        // Validar despesas
        List<DespesaConsolidada> despesasValidadas = consolidado.stream()
                .filter(validationService::isDespesaValida)
                .toList();

        // Lista para despesas sem match
        List<DespesaConsolidada> semMatch = new ArrayList<>();

        // Enriquecer despesas
        List<DespesaConsolidada> despesasEnriquecidas =
                despesaEnrichmentService.enrichDespesas(despesasValidadas, mapaOperadoras, semMatch);

        // Gravar CSV de registros sem match
        if (!semMatch.isEmpty()) {
            Path semMatchCsv = Paths.get("despesas_sem_match.csv");
            csvService.writeCsv(semMatch, semMatchCsv);
            log.info("CSV de despesas sem match gerado em {}", semMatchCsv.toAbsolutePath());
        }

        // ------------------
        // 3. Agregação e CSV final
        List<DespesaAgregada> agregadas = aggregationService.aggregate(despesasEnriquecidas);

        Path aggregatedCsv = Paths.get("despesas_agregadas.csv");
        csvService.writeAggregatedCsv(agregadas, aggregatedCsv);

        // ------------------
        // 4. Compactar CSV final
        Path zipFile = Paths.get("despesas_agregadas.zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            ZipEntry entry = new ZipEntry("despesas_agregadas.csv");
            zos.putNextEntry(entry);
            Files.copy(aggregatedCsv, zos);
            zos.closeEntry();
        }

        log.info("CSV final agregado e compactado em {}", zipFile.toAbsolutePath());
    }

    // ------------------
    // Stub para os últimos 3 trimestres
    public List<AnsDownloadDto> getUltimos3Trimestres() {
        var agora = java.time.YearMonth.now();
        List<AnsDownloadDto> dtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            var ym = agora.minusMonths(i * 3L);
            int ano = ym.getYear();
            int trimestre = (ym.getMonthValue() - 1) / 3 + 1;
            String url = construirUrlDoZip(ano, trimestre);
            dtos.add(new AnsDownloadDto(url, ano, trimestre, null));
        }
        return dtos;
    }

    private String construirUrlDoZip(int ano, int trimestre) {
        return "https://www.ans.gov.br/dados/zip_" + ano + "_Q" + trimestre + ".zip";
    }
}
