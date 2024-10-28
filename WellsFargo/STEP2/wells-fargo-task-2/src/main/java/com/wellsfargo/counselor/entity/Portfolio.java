package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
public class Portfolio {
    @Id
    @Getter
    @Column(name = "portfolio_Id", updatable = false, nullable = false)
    private String portfolioId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Getter
    @BatchSize(size = 10) // Mitigate Lazy's N+1 problem by loading a maximum of 10 clients at a time
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // Use LAZY loading to optimize performance by loading clients only when needed
    // This is particularly important for Lists, as they can lead to performance issues when accessed in large quantities.
    private List<Security> securities;

    public Portfolio() {
        this.securities = new ArrayList<>();
    }

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist(){
        if(this.creationDate == null){
            this.creationDate = LocalDateTime.now();
        }
    }
}
