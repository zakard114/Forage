package com.wellsfargo.counselor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PortfolioDTO {

    @Builder.Default
    private String portfolioId = UUID.randomUUID().toString();

    @NotNull(message = "Client ID cannot be null")
    @Size(min = 1, max = 50, message = "Client ID must be between 1 and 50 characters")
    private String clientId;

    @Builder.Default
    @NotNull(message = "Securities cannot be null")
    private List<SecurityDTO> securities = new ArrayList<>();

    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDateTime creationDate;

}
