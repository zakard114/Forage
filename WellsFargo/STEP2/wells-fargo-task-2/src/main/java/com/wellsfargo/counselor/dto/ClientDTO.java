package com.wellsfargo.counselor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class ClientDTO {

    @Builder.Default
    private String clientId = UUID.randomUUID().toString();

    @NotNull(message = "Portfolios cannot be null")
    private List<PortfolioDTO> portfolios = new ArrayList<>();

    @NotNull(message = "Advisor ID cannot be null")
    private String advisorId;

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
