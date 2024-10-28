package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Security {
    @Id
    @Getter
    @Column(name = "securityId", updatable = false, nullable = false)
    private String securityId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SecurityCategory category;

    @Column(nullable = false)
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false)
    private int quantity;

    @PrePersist
    public void prePersist() {
        if (this.purchaseDate == null){
            this.purchaseDate = LocalDateTime.now();
        }
    }


}
