package com.wellsfargo.counselor.dto;

import com.wellsfargo.counselor.entity.SecurityCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SecurityDTO {

    @Builder.Default
    private String securityId = UUID.randomUUID().toString();

    @NotNull(message = "Portfolio ID cannot be null")
    @Size(min = 1, max = 50, message = "Portfolio ID must be between 1 and 50 characters")
    private String portfolioId;

    @NotNull(message = "Security name cannot be null")
    @Size(min = 2, max = 50, message = "Security name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Category cannot be null")
    private SecurityCategory category;

    @NotNull(message = "Purchase price cannot be null")
    private BigDecimal purchasePrice;

    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDateTime purchaseDate;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;  // Change int to Integer to allow null.

}
