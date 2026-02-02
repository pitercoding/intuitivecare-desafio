package com.pitercoding.backend.dto;

import java.math.BigDecimal;

public record TotalPorUFDTO(
        String uf,
        BigDecimal total
) {}
