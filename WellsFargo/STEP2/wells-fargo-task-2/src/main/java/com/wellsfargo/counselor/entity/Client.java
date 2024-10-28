package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
public class Client {
    @Id
    @Getter
    @Column(name = "client_Id", updatable = false, nullable = false)
    private String clientId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    private Advisor advisor;

    @Getter
    @BatchSize(size = 10) // Mitigate Lazy's N+1 problem by loading a maximum of 10 clients at a time
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // Use LAZY loading to optimize performance by loading clients only when needed
    // This is particularly important for Lists, as they can lead to performance issues when accessed in large quantities.
    private List<Portfolio> portfolios;

    public Client() {
        this.portfolios = portfolios != null ? portfolios :  new ArrayList<>();
    }

    public Client(String clientId, Advisor advisor, String firstName, String lastName, String address, String phone, String email) {
        this.clientId = clientId;
        this.advisor = advisor;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

//    @PrePersist
//    public void prePersist(){
//        if(this.clientId == null) {
//            this.clientId = UUID.randomUUID().toString(); // Automatically generate ID
//        }
//    }

//    public void setFirstName(@Nonnull String firstName){
//        if (firstName.trim().isEmpty()){
//            throw new IllegalArgumentException("First name cannot be empty");
//        }
//        this.firstName = firstName;
//    }
//
//    public void setLastName(@Nonnull String lastName){
//        if (lastName.trim().isEmpty()){
//            throw new IllegalArgumentException("last name cannot be empty");
//        }
//        this.lastName = lastName;
//    }

}
