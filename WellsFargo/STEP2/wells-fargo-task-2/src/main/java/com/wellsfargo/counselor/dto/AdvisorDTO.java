package com.wellsfargo.counselor.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AdvisorDTO {

    @Builder.Default// If the service layer does not check and generate advisorId with a condition (e.g., in the createAdvisor method),
                      // the @Builder.Default in the DTO will handle automatic UUID generation when a new AdvisorDTO is created.

    // No validation required for portfolioId as it is generated automatically
    private String advisorId = UUID.randomUUID().toString();

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Address cannot be null")
    @Size(min = 10, max = 100, message = "Address must be between 10 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.-]+$", message = "Invalid address format")
    private String address;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String phone;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;


}