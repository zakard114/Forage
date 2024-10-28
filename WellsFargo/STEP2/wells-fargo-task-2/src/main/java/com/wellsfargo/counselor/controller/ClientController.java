package com.wellsfargo.counselor.controller;

import com.wellsfargo.counselor.dto.ClientDTO;
import com.wellsfargo.counselor.dto.PortfolioDTO;
import com.wellsfargo.counselor.service.ClientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO){

        log.info(clientDTO.getAdvisorId());
        if (clientDTO.getAdvisorId() == null){

            throw new IllegalArgumentException("Advisor ID cannot be null");
        }

        // Send the DTO to the service and receive the returned DTO
        ClientDTO createdClient = clientService.createClient(clientDTO);
        // Return the result
//        log.info("advisor existence testing");
//        log.info(createdClient.getAdvisorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable String clientId){
        ClientDTO clientDTO = clientService.getClientById(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllAdvisors(){
        List<ClientDTO> clientDTOList = clientService.getAllClients();
        return ResponseEntity.status(HttpStatus.OK).body(clientDTOList);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable String clientId, @Valid @RequestBody ClientDTO clientDTO){
        ClientDTO updatedClient = clientService.updateClient(clientId, clientDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId){
        clientService.deleteClient(clientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{clientId}/portfolio")
    public ResponseEntity<ClientDTO> addPortfolioToClient(@PathVariable String clientId
            , @Valid @RequestBody PortfolioDTO portfolioDTO){
        ClientDTO updatedPortfolioToClient = clientService.addPortfolioToClient(clientId, portfolioDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPortfolioToClient);
    }

    @DeleteMapping("/{clientId}/portfolio")
    public ResponseEntity<Void> removePortfolioFromClient(String clientId, PortfolioDTO portfolioDTO){
        clientService.removePortfolioFromClient(clientId, portfolioDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
