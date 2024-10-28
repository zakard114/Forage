package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Builder
@ToString
@Entity
public class Advisor {

    @Id
    @Getter
    @Column(name = "advisor_id", updatable = false, nullable = false)
    private String advisorId;

    @Setter
    @Getter
    @BatchSize(size = 10) // Mitigate Lazy's N+1 problem by loading a maximum of 10 clients at a time
    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    // Use LAZY loading to optimize performance by loading clients only when needed
    // This is particularly important for Lists, as they can lead to performance issues when accessed in large quantities.
    private List<Client> clients;

    public Advisor() {
        this.clients = clients != null ? clients : new ArrayList<>(); // Null 체크 및 초기화
    }

    public Advisor(String advisorId, String firstName, String lastName, String address, String phone, String email) {
        this.advisorId = advisorId;
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
//        if(this.advisorId == null) {
//            this.advisorId = UUID.randomUUID().toString(); // Automatically generate ID
//        }
//    }
// If the service layer does not have a check for advisorId generation,
// such as in the first "if" clause in the "createAdvisor" method,
// we must rely on either the entity's @PrePersist method or the DTO's @Builder.Default setting
// to ensure that the advisorId is generated before the entity is persisted in the database.
//
// However, if we must use the prePersist() method, it will result in an error
// if the "advisorId" field is missing during the POST request.

}