package com.example.tracker_service.dtos;

import java.math.BigDecimal;

public record PriceDropEvent(
        String productName,
        BigDecimal oldPrice,
        BigDecimal newPrice
) {}
